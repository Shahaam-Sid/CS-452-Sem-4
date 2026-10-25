import Avatar from "../ui/Avatar";
import Badge from "../ui/Badge";
import { formatDate, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

// getDisplayName / getAvatarName / getUsername receive the member ({ userId, joinedAt }).
export default function MemberList({
  members = [],
  currentUserId,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  getAvatarName = (m) => getDisplayName(m),
  getUsername = () => null,
  emptyText = "No members yet.",
  className,
}) {
  if (members.length === 0) return <p className="text-sm text-gray-500">{emptyText}</p>;

  return (
    <ul className={cn("divide-y divide-gray-100", className)}>
      {members.map((m) => {
        const username = getUsername(m);
        const details = [username && `@${username}`, m.joinedAt && `Joined ${formatDate(m.joinedAt)}`]
          .filter(Boolean)
          .join(" · ");

        return (
          <li key={m.userId} className="flex items-center gap-3 py-3">
            <Avatar name={getAvatarName(m)} />
            <div className="min-w-0 flex-1">
              <p className="truncate text-sm font-medium text-gray-900">{getDisplayName(m)}</p>
              {details && <p className="truncate text-xs text-gray-500">{details}</p>}
            </div>
            {currentUserId && m.userId === currentUserId && <Badge variant="info">You</Badge>}
          </li>
        );
      })}
    </ul>
  );
}