import { cn } from "../../utils/cn";

const variants = {
  error: "border-red-200 bg-red-50 text-red-700",
  success: "border-brand-200 bg-brand-50 text-brand-800",
  info: "border-blue-200 bg-blue-50 text-blue-700",
  warning: "border-amber-200 bg-amber-50 text-amber-800",
};

export default function Alert({ variant = "info", title, className, children }) {
  if (!title && !children) return null;
  return (
    <div
      role={variant === "error" ? "alert" : "status"}
      className={cn("rounded-lg border px-4 py-3 text-sm", variants[variant], className)}
    >
      {title && <p className="font-medium">{title}</p>}
      {children && <div className={title ? "mt-0.5" : ""}>{children}</div>}
    </div>
  );
}