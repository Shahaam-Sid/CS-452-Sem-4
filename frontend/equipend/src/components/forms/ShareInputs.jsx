import Input from "../ui/Input";
import { formatCurrencyCode, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

// Controlled. members: the SELECTED members [{ userId }]; value: { [userId]: "12.50" }
// mode: "PERCENTAGE" or "AMOUNT". The backend validates and calculates; this only collects input.
export default function ShareInputs({
  members = [],
  value = {},
  onChange,
  mode,
  currency,
  currentUserId,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  error,
  className,
}) {
  const isPercent = mode === "PERCENTAGE";
  const unit = isPercent ? "%" : formatCurrencyCode(currency);
  const setShare = (id, v) => onChange({ ...value, [id]: v });

  return (
    <div className={cn("space-y-2", className)}>
      <span className="block text-sm font-medium text-gray-700">
        {isPercent ? "Percentage per person" : "Amount per person"}
      </span>

      <div className="space-y-2 rounded-lg border border-gray-200 p-3">
        {members.length === 0 && <p className="text-sm text-gray-500">Select people above first.</p>}
        {members.map((m) => (
          <div key={m.userId} className="flex items-center justify-between gap-3">
            <span className="min-w-0 flex-1 truncate text-sm text-gray-700">
              {getDisplayName(m)}
              {m.userId === currentUserId ? " (you)" : ""}
            </span>
            <div className="flex w-36 items-center gap-2">
              <Input
                inputMode="decimal"
                size="sm"
                placeholder="0.00"
                aria-label={`Share for ${getDisplayName(m)}`}
                value={value[m.userId] ?? ""}
                onChange={(e) => setShare(m.userId, e.target.value)}
              />
              <span className="w-9 text-xs text-gray-500">{unit}</span>
            </div>
          </div>
        ))}
      </div>

      {error && <p className="text-xs text-red-600">{error}</p>}
    </div>
  );
}