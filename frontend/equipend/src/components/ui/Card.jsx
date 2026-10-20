import { cn } from "../../utils/cn";

const paddings = { none: "", sm: "p-3", md: "p-5", lg: "p-8" };

export default function Card({
  title,
  subtitle,
  actions,
  footer,
  padding = "md",
  hoverable = false,
  className,
  children,
  ...rest
}) {
  const hasHeader = title || subtitle || actions;
  return (
    <div
      className={cn(
        "rounded-xl border border-gray-200 bg-white shadow-sm",
        hoverable && "transition-shadow hover:shadow-md",
        className
      )}
      {...rest}
    >
      {hasHeader && (
        <div className={cn("flex items-start justify-between gap-3", paddings[padding], "pb-0")}>
          <div>
            {title && <h3 className="text-base font-semibold text-gray-900">{title}</h3>}
            {subtitle && <p className="text-sm text-gray-500">{subtitle}</p>}
          </div>
          {actions}
        </div>
      )}
      <div className={cn(paddings[padding], hasHeader && padding !== "none" && "pt-4")}>{children}</div>
      {footer && <div className="rounded-b-xl border-t border-gray-100 bg-gray-50 px-5 py-3">{footer}</div>}
    </div>
  );
}