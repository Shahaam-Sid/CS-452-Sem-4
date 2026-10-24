import { Link } from "react-router-dom";
import { CalendarDays } from "lucide-react";
import Card from "../ui/Card";
import Badge from "../ui/Badge";
import { formatCurrencyCode, formatDate } from "../../utils/format";
import { cn } from "../../utils/cn";

export default function GroupCard({ group, to, className }) {
  const card = (
    <Card hoverable className={cn("h-full", className)}>
      <div className="flex items-start justify-between gap-3">
        <h3 className="truncate text-base font-semibold text-gray-900">{group.name}</h3>
        <Badge variant="success">{formatCurrencyCode(group.currency)}</Badge>
      </div>

      <p className="mt-1 line-clamp-2 min-h-10 text-sm text-gray-500">
        {group.description || "No description"}
      </p>

      {group.joinedAt && (
        <p className="mt-4 flex items-center gap-1 text-xs text-gray-500">
          <CalendarDays size={14} /> Joined {formatDate(group.joinedAt)}
        </p>
      )}
    </Card>
  );

  return to ? (
    <Link to={to} className="block h-full">
      {card}
    </Link>
  ) : (
    card
  );
}