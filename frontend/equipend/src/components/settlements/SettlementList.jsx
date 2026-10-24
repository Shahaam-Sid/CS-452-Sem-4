import SettlementItem from "./SettlementItem";
import { cn } from "../../utils/cn";

// getItemProps(settlement) returns extra props for each SettlementItem (currency, currentUserId...).
export default function SettlementList({
  settlements = [],
  getItemProps = () => ({}),
  emptyText = "No payments recorded yet.",
  className,
}) {
  if (settlements.length === 0) return <p className="text-sm text-gray-500">{emptyText}</p>;

  return (
    <ul className={cn("divide-y divide-gray-100", className)}>
      {settlements.map((s) => (
        <li key={s.id}>
          <SettlementItem settlement={s} {...getItemProps(s)} />
        </li>
      ))}
    </ul>
  );
}