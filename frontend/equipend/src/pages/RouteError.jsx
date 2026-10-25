import { isRouteErrorResponse, Link, useRouteError } from "react-router-dom";
import { TriangleAlert } from "lucide-react";
import Button from "../components/ui/Button";
import EmptyState from "../components/ui/EmptyState";

export default function RouteError({ fullScreen = true, homePath = "/", homeLabel = "Go home" }) {
  const error = useRouteError();
  const isResponse = isRouteErrorResponse(error);

  const title = isResponse && error.status === 404 ? "Page not found" : "Something went wrong";
  const description = isResponse
    ? `${error.status} ${error.statusText}`
    : error?.message || "An unexpected error occurred.";

  const content = (
    <EmptyState
      icon={<TriangleAlert size={24} />}
      title={title}
      description={description}
      action={
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => window.location.reload()}>
            Reload page
          </Button>
          <Link to={homePath}>
            <Button>{homeLabel}</Button>
          </Link>
        </div>
      }
    />
  );

  return fullScreen ? (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 p-6">{content}</div>
  ) : (
    content
  );
}