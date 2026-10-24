import Checkbox from "../ui/Checkbox";
import Button from "../ui/Button";
import { shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

// Controlled. members: [{ userId }]; value: array of selected userIds.
export default function ParticipantSelector({
  members = [],
  value = [],
  onChange,
  currentUserId,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  label = "Split between",
  error,
  className,
}) {
  const selected = new Set(value);
  const toggle = (id) => onChange(selected.has(id) ? value.filter((v) => v !== id) : [...value, id]);

  return (
    <div className={cn("space-y-2", className)}>
      <div className="flex items-center justify-between">
        <span className="text-sm font-medium text-gray-700">{label}</span>
        <div className="flex gap-1">
          <Button size="sm" variant="ghost" onClick={() => onChange(members.map((m) => m.userId))}>
            All
          </Button>
          <Button size="sm" variant="ghost" onClick={() => onChange([])}>
            None
          </Button>
        </div>
      </div>

      <div className="max-h-48 space-y-2 overflow-y-auto rounded-lg border border-gray-200 p-3">
        {members.map((m) => (
          <Checkbox
            key={m.userId}
            checked={selected.has(m.userId)}
            onChange={() => toggle(m.userId)}
            label={`${getDisplayName(m)}${m.userId === currentUserId ? " (you)" : ""}`}
          />
        ))}
      </div>

      {error && <p className="text-xs text-red-600">{error}</p>}
    </div>
  );
}