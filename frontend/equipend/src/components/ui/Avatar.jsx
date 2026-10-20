import { cn } from "../../utils/cn";

const sizes = { xs: "h-6 w-6 text-[10px]", sm: "h-8 w-8 text-xs", md: "h-10 w-10 text-sm", lg: "h-14 w-14 text-lg" };
const palette = [
  "bg-brand-100 text-brand-800",
  "bg-blue-100 text-blue-800",
  "bg-amber-100 text-amber-800",
  "bg-rose-100 text-rose-800",
  "bg-violet-100 text-violet-800",
];

const initialsOf = (name = "") =>
  name.trim().split(/\s+/).slice(0, 2).map((p) => p[0]?.toUpperCase()).join("") || "?";

const colorOf = (name = "") =>
  palette[[...name].reduce((sum, ch) => sum + ch.charCodeAt(0), 0) % palette.length];

export default function Avatar({ name, src, size = "md", className }) {
  if (src) {
    return <img src={src} alt={name} className={cn("rounded-full object-cover", sizes[size], className)} />;
  }
  return (
    <span
      title={name}
      className={cn(
        "inline-flex shrink-0 items-center justify-center rounded-full font-semibold",
        sizes[size],
        colorOf(name),
        className
      )}
    >
      {initialsOf(name)}
    </span>
  );
}