import { createBrowserRouter } from "react-router-dom";
import RootLayout from "../components/layouts/RootLayout";
import DashboardLayout from "../components/layouts/DashboardLayout";
import RootRedirect from "../components/RootRedirect";
import GuestOnlyRoute from "../components/GuestOnlyRoute";
import ProtectedRoute from "../components/ProtectedRoute";
import DoorStep from "../pages/DoorStep";
import SignupPage from "../pages/SignupPage";
import LoginPage from "../pages/LoginPage";
import Dashboard from "../pages/Dashboard";
import Groups from "../pages/Groups";
import Expenses from "../pages/Expenses";
import SettleUp from "../pages/SettleUp";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
      { index: true, element: <RootRedirect /> },
      { path: "door-step", element: <DoorStep /> },
      { path: "signup", element: <GuestOnlyRoute><SignupPage /></GuestOnlyRoute> },
      { path: "login", element: <GuestOnlyRoute><LoginPage /></GuestOnlyRoute> },
      {
        path: "dashboard",
        element: <ProtectedRoute><DashboardLayout /></ProtectedRoute>,
        children: [
          { index: true, element: <Dashboard />, handle: { title: "Dashboard" } },
          { path: "groups", element: <Groups />, handle: { title: "Groups" } },
          { path: "expenses", element: <Expenses />, handle: { title: "Expenses" } },
          { path: "settle", element: <SettleUp />, handle: { title: "Settle Up" } },
        ],
      },
    ],
  },
]);