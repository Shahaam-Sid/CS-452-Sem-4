import BalanceCard from "./BalanceCard";
import { cn } from "../../utils/cn";

export default function BalanceList({
  rows = [],
  getDisplayName,
  getAvatarName,
  groupNames,
  emptyText = "No balances yet.",
  className,
}) {
  if (rows.length === 0) return <p className="text-sm text-gray-500">{emptyText}</p>;

  return (
    <div className={cn("grid gap-4 md:grid-cols-2", className)}>
      {rows.map((r) => (
        <BalanceCard
          key={r.key}
          row={r}
          getDisplayName={getDisplayName}
          getAvatarName={getAvatarName}
          groupNames={groupNames}
        />
      ))}
    </div>
  );
}