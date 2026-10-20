import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function RootRedirect({ authedPath = "/dashboard", guestPath = "/door-step" }) {
  const { isAuthenticated } = useAuth();
  return <Navigate to={isAuthenticated ? authedPath : guestPath} replace />;
}