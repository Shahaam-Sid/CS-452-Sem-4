const cents = (n) => Math.round(Number(n) * 100);

// Builds the line items for ONE group, keeping only those that involve `me` (SRS FR-4.5).
// A line item is a debt: debtorId owes creditorId `cents`.
export function buildEntries({
  me,
  groupId,
  currency,
  expenses = [], // the group's expenses
  mySplits = [], // my splits across all groups: [{ expenseId, shareAmount }]
  splitsByExpense = new Map(), // expenseId -> splits, for the expenses I paid
  settlements = [], // the group's settlements
}) {
  const entries = [];
  const myShare = new Map(mySplits.map((s) => [s.expenseId, s.shareAmount]));

  for (const e of expenses) {
    const base = {
      kind: "expense",
      groupId,
      currency,
      expenseId: e.id,
      title: e.title,
      date: e.createdAt,
    };

    if (e.paidBy === me) {
      // others owe me their share of what I paid (my own share is not a debt)
      for (const s of splitsByExpense.get(e.id) ?? []) {
        if (s.userId === me) continue;
        entries.push({ ...base, debtorId: s.userId, creditorId: me, cents: cents(s.shareAmount) });
      }
    } else if (myShare.has(e.id)) {
      // I owe whoever paid my share (other people's shares are not my business)
      entries.push({ ...base, debtorId: me, creditorId: e.paidBy, cents: cents(myShare.get(e.id)) });
    }
  }

  for (const s of settlements) {
    if (s.paidBy !== me && s.paidTo !== me) continue;
    // paying someone reduces what the payer owes the receiver
    entries.push({
      kind: "settlement",
      groupId,
      currency,
      settlementId: s.id,
      note: s.note,
      date: s.timestamp,
      debtorId: s.paidBy,
      creditorId: s.paidTo,
      cents: cents(s.amount),
    });
  }

  return entries;
}

const emptyDirection = () => ({ owed: 0, settled: 0, outstanding: 0, expenses: [], settlements: [] });
const newestFirst = (a, b) => new Date(b.date) - new Date(a.date);

// One row per (other person, currency), each with two separate directions (SRS FR-4.3).
// youOwe: me -> them. theyOwe: them -> me. Amounts are integer cents.
export function computeBalances(entries, me) {
  const rows = new Map();

  for (const entry of entries) {
    const youAreDebtor = entry.debtorId === me;
    const other = youAreDebtor ? entry.creditorId : entry.debtorId;
    const key = `${other}|${entry.currency}`;

    if (!rows.has(key)) {
      rows.set(key, {
        key,
        userId: other,
        currency: entry.currency,
        youOwe: emptyDirection(),
        theyOwe: emptyDirection(),
      });
    }

    const row = rows.get(key);
    const direction = youAreDebtor ? row.youOwe : row.theyOwe;
    if (entry.kind === "expense") {
      direction.owed += entry.cents;
      direction.expenses.push(entry);
    } else {
      direction.settled += entry.cents;
      direction.settlements.push(entry);
    }
  }

  const result = [...rows.values()];
  for (const row of result) {
    for (const d of [row.youOwe, row.theyOwe]) {
      d.outstanding = d.owed - d.settled;
      d.expenses.sort(newestFirst);
      d.settlements.sort(newestFirst);
    }
  }

  const biggest = (r) => Math.max(r.youOwe.outstanding, r.theyOwe.outstanding);
  return result.sort((a, b) => biggest(b) - biggest(a));
}

// Totals per currency for the dashboard header. Each direction is summed separately.
export function summarizeBalances(rows = []) {
  const byCurrency = {};
  for (const r of rows) {
    const t = (byCurrency[r.currency] ??= { youOwe: 0, theyOwe: 0 });
    if (r.youOwe.outstanding > 0) t.youOwe += r.youOwe.outstanding;
    if (r.theyOwe.outstanding > 0) t.theyOwe += r.theyOwe.outstanding;
  }
  return Object.entries(byCurrency).map(([currency, t]) => ({ currency, ...t }));
}