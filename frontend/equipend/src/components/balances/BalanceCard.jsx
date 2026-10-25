import Avatar from "../ui/Avatar";
import Card from "../ui/Card";
import { formatCents, formatDate, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

function DirectionBlock({ title, direction, currency, tone, groupNames }) {
  const { owed, settled, outstanding, expenses, settlements } = direction;
  if (expenses.length + settlements.length === 0) return null;

  const groupLabel = (groupId) => (groupNames?.get(groupId) ? ` · ${groupNames.get(groupId)}` : "");

  let status;
  if (outstanding > 0) {
    status = (
      <span className={cn("text-base font-semibold", tone === "owe" ? "text-red-600" : "text-brand-700")}>
        {formatCents(outstanding, currency)}
      </span>
    );
  } else if (outstanding === 0) {
    status = <span className="text-sm font-medium text-gray-500">Settled up</span>;
  } else {
    status = (
      <span className="text-sm font-medium text-amber-700">
        Overpaid by {formatCents(-outstanding, currency)}
      </span>
    );
  }

  return (
    <div className="rounded-lg border border-gray-200 p-3">
      <div className="flex items-center justify-between gap-3">
        <p className="text-sm text-gray-700">{title}</p>
        {status}
      </div>
      <p className="mt-0.5 text-xs text-gray-500">
        Expenses {formatCents(owed, currency)} − payments {formatCents(settled, currency)}
      </p>

      <details className="mt-2">
        <summary className="cursor-pointer text-xs font-medium text-brand-700">Details</summary>
        <ul className="mt-2 divide-y divide-gray-100">
          {expenses.map((e) => (
            <li key={`e-${e.expenseId}-${e.debtorId}`} className="flex justify-between gap-3 py-1.5 text-xs">
              <span className="min-w-0 truncate text-gray-700">
                {e.title}
                <span className="text-gray-400">
                  {" "}
                  · {formatDate(e.date)}
                  {groupLabel(e.groupId)}
                </span>
              </span>
              <span className="shrink-0 text-gray-900">{formatCents(e.cents, currency)}</span>
            </li>
          ))}
          {settlements.map((s) => (
            <li key={`s-${s.settlementId}`} className="flex justify-between gap-3 py-1.5 text-xs">
              <span className="min-w-0 truncate text-gray-700">
                Payment{s.note ? `: ${s.note}` : ""}
                <span className="text-gray-400">
                  {" "}
                  · {formatDate(s.date)}
                  {groupLabel(s.groupId)}
                </span>
              </span>
              <span className="shrink-0 text-brand-700">−{formatCents(s.cents, currency)}</span>
            </li>
          ))}
        </ul>
      </details>
    </div>
  );
}

// row: one entry from computeBalances. getDisplayName / getAvatarName take a user id.
export default function BalanceCard({
  row,
  getDisplayName = (userId) => `Member ${shortId(userId)}`,
  getAvatarName = (userId) => shortId(userId),
  groupNames, // optional Map(groupId -> name) to label each line item
  className,
}) {
  const name = getDisplayName(row.userId);

  return (
    <Card className={className}>
      <div className="mb-3 flex items-center gap-3">
        <Avatar name={getAvatarName(row.userId)} size="sm" />
        <p className="truncate text-sm font-semibold text-gray-900">{name}</p>
      </div>

      <div className="space-y-3">
        <DirectionBlock
          title={`You owe ${name}`}
          direction={row.youOwe}
          currency={row.currency}
          tone="owe"
          groupNames={groupNames}
        />
        <DirectionBlock
          title={`${name} owes you`}
          direction={row.theyOwe}
          currency={row.currency}
          tone="owed"
          groupNames={groupNames}
        />
      </div>
    </Card>
  );
}