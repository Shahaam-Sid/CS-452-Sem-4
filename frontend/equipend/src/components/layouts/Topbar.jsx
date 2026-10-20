import { useEffect, useRef, useState } from "react";
import { ChevronDown, LogOut, Menu } from "lucide-react";
import Avatar from "../ui/Avatar";
import { cn } from "../../utils/cn";

// user: { name, email, avatarUrl? }   menuItems: [{ label, onClick, icon? }]
export default function Topbar({ title, onMenuClick, user, onLogout, menuItems = [], rightSlot, className }) {
  const [open, setOpen] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    const handler = (e) => ref.current && !ref.current.contains(e.target) && setOpen(false);
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  return (
    <header className={cn("sticky top-0 z-20 flex h-16 items-center gap-3 border-b border-gray-200 bg-white/90 px-4 backdrop-blur sm:px-6", className)}>
      <button onClick={onMenuClick} className="rounded-md p-2 text-gray-600 hover:bg-gray-100 lg:hidden" aria-label="Open menu">
        <Menu size={20} />
      </button>

      <h1 className="flex-1 truncate text-lg font-semibold text-gray-900">{title}</h1>

      {rightSlot}

      {user && (
        <div ref={ref} className="relative">
          <button onClick={() => setOpen((o) => !o)} className="flex items-center gap-2 rounded-lg p-1.5 hover:bg-gray-100">
            <Avatar name={user.name} src={user.avatarUrl} size="sm" />
            <span className="hidden text-sm font-medium text-gray-700 sm:block">{user.name}</span>
            <ChevronDown size={16} className="text-gray-400" />
          </button>

          {open && (
            <div className="absolute right-0 mt-2 w-56 rounded-lg border border-gray-200 bg-white py-1 shadow-lg">
              <div className="border-b border-gray-100 px-4 py-2">
                <p className="text-sm font-medium text-gray-900">{user.name}</p>
                {user.email && <p className="truncate text-xs text-gray-500">{user.email}</p>}
              </div>
              {menuItems.map(({ label, onClick, icon }) => (
                <button
                  key={label}
                  onClick={() => { setOpen(false); onClick?.(); }}
                  className="flex w-full items-center gap-2 px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-50"
                >
                  {icon}{label}
                </button>
              ))}
              {onLogout && (
                <button onClick={onLogout} className="flex w-full items-center gap-2 px-4 py-2 text-left text-sm text-red-600 hover:bg-red-50">
                  <LogOut size={16} /> Log out
                </button>
              )}
            </div>
          )}
        </div>
      )}
    </header>
  );
}