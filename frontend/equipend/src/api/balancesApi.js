import { getExpenseSplits, getGroupExpenses, getMySplits } from "./expensesApi";
import { getGroupSettlements } from "./settlementsApi";
import { buildEntries } from "../utils/balances";

// Requests per group: expenses + settlements, plus one splits call for each expense I paid.
// My own shares of other people's expenses come from a single /splits/user call (mySplits).
async function entriesForGroup(group, userId, mySplits) {
  const [expenses, settlements] = await Promise.all([
    getGroupExpenses(group.id),
    getGroupSettlements(group.id),
  ]);

  const paidByMe = expenses.filter((e) => e.paidBy === userId);
  const splitLists = await Promise.all(paidByMe.map((e) => getExpenseSplits(e.id)));
  const splitsByExpense = new Map(paidByMe.map((e, i) => [e.id, splitLists[i]]));

  return buildEntries({
    me: userId,
    groupId: group.id,
    currency: group.currency,
    expenses,
    mySplits,
    splitsByExpense,
    settlements,
  });
}

// group: { id, currency }
export async function getGroupBalanceEntries(group, userId) {
  const mySplits = await getMySplits(userId);
  return entriesForGroup(group, userId, mySplits);
}

// groups: every group the user belongs to
export async function getAllBalanceEntries(groups, userId) {
  const mySplits = await getMySplits(userId);
  const lists = await Promise.all(groups.map((g) => entriesForGroup(g, userId, mySplits)));
  return lists.flat();
}