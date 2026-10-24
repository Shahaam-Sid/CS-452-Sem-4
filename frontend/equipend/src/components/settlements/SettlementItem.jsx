import { ArrowRight } from "lucide-react";
import { formatDate, formatMoney, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

export default function SettlementItem({
  settlement,
  currency,
  currentUserId,
  getDisplayName = (userId) => `Member ${shortId(userId)}`,
  className,
}) {
  const nameOf = (id) => (id === currentUserId ? "You" : getDisplayName(id));

  return (
    <div className={cn("flex items-start justify-between gap-4 px-1 py-3", className)}>
      <div className="min-w-0">
        <p className="flex flex-wrap items-center gap-1.5 text-sm font-medium text-gray-900">
          <span>{nameOf(settlement.paidBy)}</span>
          <ArrowRight size={14} className="text-gray-400" />
          <span>{nameOf(settlement.paidTo)}</span>
        </p>
        <p className="mt-0.5 truncate text-xs text-gray-500">
          {settlement.note ? `${settlement.note} · ` : ""}
          {formatDate(settlement.timestamp)}
        </p>
      </div>
      <p className="shrink-0 text-sm font-semibold text-gray-900">
        {formatMoney(settlement.amount, currency)}
      </p>
    </div>
  );
}