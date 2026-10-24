import Avatar from "../ui/Avatar";
import Badge from "../ui/Badge";
import { formatDate, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

export default function MemberList({
  members = [],
  currentUserId,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  getAvatarName = (m) => shortId(m.userId),
  emptyText = "No members yet.",
  className,
}) {
  if (members.length === 0) return <p className="text-sm text-gray-500">{emptyText}</p>;

  return (
    <ul className={cn("divide-y divide-gray-100", className)}>
      {members.map((m) => (
        <li key={m.userId} className="flex items-center gap-3 py-3">
          <Avatar name={getAvatarName(m)} />
          <div className="min-w-0 flex-1">
            <p className="truncate text-sm font-medium text-gray-900">{getDisplayName(m)}</p>
            {m.joinedAt && <p className="text-xs text-gray-500">Joined {formatDate(m.joinedAt)}</p>}
          </div>
          {currentUserId && m.userId === currentUserId && <Badge variant="info">You</Badge>}
        </li>
      ))}
    </ul>
  );
}