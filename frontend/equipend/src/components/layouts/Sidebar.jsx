import { NavLink } from "react-router-dom";
import { X } from "lucide-react";
import { cn } from "../../utils/cn";

// links: [{ label, to, icon: <Icon/>, end?: boolean }]
export default function Sidebar({ links = [], brand, open, onClose, footer, className }) {
  return (
    <>
      {open && <div className="fixed inset-0 z-30 bg-black/40 lg:hidden" onClick={onClose} aria-hidden="true" />}
      <aside
        className={cn(
          "fixed inset-y-0 left-0 z-40 flex w-64 flex-col border-r border-gray-200 bg-white",
          "transition-transform duration-200 lg:translate-x-0",
          open ? "translate-x-0" : "-translate-x-full",
          className
        )}
      >
        <div className="flex h-16 items-center justify-between px-5">
          <div className="text-lg font-bold text-brand-700">{brand}</div>
          <button onClick={onClose} className="rounded-md p-1 text-gray-500 hover:bg-gray-100 lg:hidden" aria-label="Close menu">
            <X size={18} />
          </button>
        </div>

        <nav className="flex-1 space-y-1 overflow-y-auto px-3 py-2">
          {links.map(({ label, to, icon, end }) => (
            <NavLink
              key={to}
              to={to}
              end={end}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
                  isActive ? "bg-brand-50 text-brand-700" : "text-gray-600 hover:bg-gray-100 hover:text-gray-900"
                )
              }
            >
              {icon}
              {label}
            </NavLink>
          ))}
        </nav>

        {footer && <div className="border-t border-gray-100 p-3">{footer}</div>}
      </aside>
    </>
  );
}