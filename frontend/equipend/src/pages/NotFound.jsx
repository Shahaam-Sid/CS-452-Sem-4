import { Link } from "react-router-dom";
import { SearchX } from "lucide-react";
import Button from "../components/ui/Button";
import EmptyState from "../components/ui/EmptyState";

export default function NotFound({ fullScreen = true, homePath = "/", homeLabel = "Go home" }) {
  const content = (
    <EmptyState
      icon={<SearchX size={24} />}
      title="Page not found"
      description="The page you are looking for does not exist or has moved."
      action={
        <Link to={homePath}>
          <Button>{homeLabel}</Button>
        </Link>
      }
    />
  );

  return fullScreen ? (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 p-6">{content}</div>
  ) : (
    content
  );
}