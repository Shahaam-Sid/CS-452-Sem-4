import { CircleCheck, CircleX, Info, TriangleAlert, X } from "lucide-react";
import { cn } from "../../utils/cn";

const variants = {
  success: { style: "border-brand-200 bg-brand-50 text-brand-800", icon: CircleCheck },
  error: { style: "border-red-200 bg-red-50 text-red-700", icon: CircleX },
  info: { style: "border-blue-200 bg-blue-50 text-blue-700", icon: Info },
  warning: { style: "border-amber-200 bg-amber-50 text-amber-800", icon: TriangleAlert },
};

export default function Toast({ variant = "info", message, onClose, className }) {
  const { style, icon: Icon } = variants[variant] ?? variants.info;

  return (
    <div
      role={variant === "error" ? "alert" : "status"}
      className={cn(
        "pointer-events-auto flex w-80 max-w-[calc(100vw-2rem)] items-start gap-3 rounded-lg border px-4 py-3 text-sm shadow-lg",
        style,
        className
      )}
    >
      <Icon size={18} className="mt-0.5 shrink-0" />
      <p className="min-w-0 flex-1">{message}</p>
      {onClose && (
        <button
          type="button"
          onClick={onClose}
          aria-label="Dismiss"
          className="shrink-0 rounded p-0.5 opacity-70 hover:opacity-100"
        >
          <X size={16} />
        </button>
      )}
    </div>
  );
}