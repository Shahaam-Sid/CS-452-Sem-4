import { cn } from "../../utils/cn";

const variants = {
  neutral: "bg-gray-100 text-gray-700",
  success: "bg-brand-100 text-brand-800",
  danger: "bg-red-100 text-red-700",
  warning: "bg-amber-100 text-amber-800",
  info: "bg-blue-100 text-blue-700",
};
const sizes = { sm: "px-2 py-0.5 text-xs", md: "px-2.5 py-1 text-sm" };

export default function Badge({ variant = "neutral", size = "sm", className, children }) {
  return (
    <span className={cn("inline-flex items-center rounded-full font-medium", variants[variant], sizes[size], className)}>
      {children}
    </span>
  );
}