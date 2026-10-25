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
import GroupDetail from "../pages/GroupDetail";
import Expenses from "../pages/Expenses";
import NotFound from "../pages/NotFound";
import RouteError from "../pages/RouteError";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
      {
        // pathless: errors below render INSIDE RootLayout, so auth and toasts still work
        errorElement: <RouteError />,
        children: [
          { index: true, element: <RootRedirect /> },
          { path: "door-step", element: <DoorStep /> },
          { path: "signup", element: <GuestOnlyRoute><SignupPage /></GuestOnlyRoute> },
          { path: "login", element: <GuestOnlyRoute><LoginPage /></GuestOnlyRoute> },
          {
            path: "dashboard",
            element: <ProtectedRoute><DashboardLayout /></ProtectedRoute>,
            children: [
              {
                // pathless: a crashing page keeps the sidebar and top bar
                errorElement: <RouteError fullScreen={false} homePath="/dashboard" homeLabel="Back to dashboard" />,
                children: [
                  { index: true, element: <Dashboard />, handle: { title: "Dashboard" } },
                  { path: "groups", element: <Groups />, handle: { title: "Groups" } },
                  { path: "groups/:groupId", element: <GroupDetail />, handle: { title: "Group Details" } },
                  { path: "expenses", element: <Expenses />, handle: { title: "Expenses" } },
                  {
                    path: "*",
                    element: <NotFound fullScreen={false} homePath="/dashboard" homeLabel="Back to dashboard" />,
                    handle: { title: "Not found" },
                  },
                ],
              },
            ],
          },
          { path: "*", element: <NotFound /> },
        ],
      },
    ],
  },
]);