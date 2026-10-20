import { Outlet } from "react-router-dom";
import Card from "../ui/Card";
import { cn } from "../../utils/cn";

export default function AuthLayout({ brand, tagline, children, className }) {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-brand-50 to-white p-4">
      <div className={cn("w-full max-w-md", className)}>
        <div className="mb-6 text-center">
          <div className="text-2xl font-bold text-brand-700">{brand}</div>
          {tagline && <p className="mt-1 text-sm text-gray-500">{tagline}</p>}
        </div>
        <Card padding="lg">{children ?? <Outlet />}</Card>
      </div>
    </div>
  );
}