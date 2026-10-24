import { LayoutDashboard, Users, Receipt } from "lucide-react";

export const dashboardLinks = [
  { label: "Dashboard", to: "/dashboard", icon: <LayoutDashboard size={18} />, end: true },
  { label: "Groups", to: "/dashboard/groups", icon: <Users size={18} /> },
  { label: "Expenses", to: "/dashboard/expenses", icon: <Receipt size={18} /> },
  // Settle Up is added in its own batch
];