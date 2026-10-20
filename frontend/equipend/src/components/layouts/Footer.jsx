import { cn } from "../../utils/cn";

// links: [{ label, href }]
export default function Footer({ text = `© ${new Date().getFullYear()} SplitEase`, links = [], className }) {
  return (
    <footer className={cn("flex flex-wrap items-center justify-between gap-2 border-t border-gray-200 px-4 py-4 text-sm text-gray-500 sm:px-6", className)}>
      <span>{text}</span>
      <div className="flex gap-4">
        {links.map(({ label, href }) => (
          <a key={href} href={href} target="_blank" rel="noreferrer" className="hover:text-brand-700">
            {label}
          </a>
        ))}
      </div>
    </footer>
  );
}