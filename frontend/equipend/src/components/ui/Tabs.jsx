import { cn } from "../../utils/cn";

// tabs: [{ id, label, count? }]
export default function Tabs({ tabs = [], value, onChange, className }) {
  return (
    <div role="tablist" className={cn("flex gap-1 border-b border-gray-200", className)}>
      {tabs.map((t) => {
        const active = t.id === value;
        return (
          <button
            key={t.id}
            type="button"
            role="tab"
            aria-selected={active}
            onClick={() => onChange(t.id)}
            className={cn(
              "-mb-px border-b-2 px-4 py-2 text-sm font-medium transition-colors",
              active
                ? "border-brand-600 text-brand-700"
                : "border-transparent text-gray-500 hover:text-gray-700"
            )}
          >
            {t.label}
            {t.count != null && (
              <span className="ml-2 rounded-full bg-gray-100 px-2 py-0.5 text-xs text-gray-600">{t.count}</span>
            )}
          </button>
        );
      })}
    </div>
  );
}