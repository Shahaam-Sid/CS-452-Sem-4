import { Navigate, Outlet } from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function GuestOnlyRoute({ redirectTo = "/dashboard", children }) {
  const { isAuthenticated } = useAuth();

  if (isAuthenticated) return <Navigate to={redirectTo} replace />;
  return children ?? <Outlet />;
}