import { cn } from "../../utils/cn";

const sizes = { sm: "h-4 w-4", md: "h-5 w-5", lg: "h-8 w-8" };

export default function Spinner({ size = "md", className }) {
  return (
    <svg
      className={cn("animate-spin", sizes[size], className)}
      viewBox="0 0 24 24"
      fill="none"
      role="status"
      aria-label="Loading"
    >
      <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" className="opacity-25" />
      <path d="M4 12a8 8 0 018-8" stroke="currentColor" strokeWidth="4" strokeLinecap="round" className="opacity-75" />
    </svg>
  );
}