import { Link } from "react-router-dom";
import Badge from "../ui/Badge";
import { formatDate, formatMoney, humanize, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

export default function ExpenseItem({
  expense,
  currency,
  currentUserId,
  getDisplayName = (userId) => `Member ${shortId(userId)}`,
  share, // the current user's share, when known
  groupName,
  to,
  className,
}) {
  const payer = expense.paidBy === currentUserId ? "You" : getDisplayName(expense.paidBy);

  const content = (
    <div className={cn("flex items-start justify-between gap-4 px-1 py-3", className)}>
      <div className="min-w-0">
        <p className="truncate text-sm font-medium text-gray-900">{expense.title}</p>
        <p className="mt-0.5 text-xs text-gray-500">
          {payer} paid · {formatDate(expense.createdAt)}
          {groupName ? ` · ${groupName}` : ""}
        </p>
      </div>
      <div className="shrink-0 text-right">
        <p className="text-sm font-semibold text-gray-900">{formatMoney(expense.amount, currency)}</p>
        {share != null && (
          <p className="text-xs text-brand-700">Your share {formatMoney(share, currency)}</p>
        )}
        {expense.splitType && <Badge className="mt-1">{humanize(expense.splitType)}</Badge>}
      </div>
    </div>
  );

  return to ? (
    <Link to={to} className="block rounded-lg hover:bg-gray-50">
      {content}
    </Link>
  ) : (
    content
  );
}