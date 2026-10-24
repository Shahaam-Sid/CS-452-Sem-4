import { Navigate, Outlet, useLocation } from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function GuestOnlyRoute({ redirectTo = "/dashboard", children }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (isAuthenticated) {
    const from = location.state?.from;
    const target = from ? `${from.pathname}${from.search ?? ""}` : redirectTo;
    return <Navigate to={target} replace />;
  }
  return children ?? <Outlet />;
}