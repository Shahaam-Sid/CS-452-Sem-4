import { useState, useEffect } from "react";
import { Outlet, useLocation, useMatches, useNavigate } from "react-router-dom";
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";
import Footer from "./Footer";
import useAuth from "../../hooks/useAuth";
import { dashboardLinks } from "../../config/navLinks";
import { APP_NAME, GITHUB_URL } from "../../config/app";

export default function DashboardLayout({
  links = dashboardLinks,
  brand = APP_NAME,
  userMenuItems,
  topbarRight,
  sidebarFooter,
  footerText,
  footerLinks = [{ label: "GitHub", href: GITHUB_URL }],
  logoutRedirect = "/door-step",
}) {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { pathname } = useLocation();
  const matches = useMatches();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  // title comes from the deepest matched route that defines handle.title
  const title = [...matches].reverse().find((m) => m.handle?.title)?.handle.title ?? "";

  // close the mobile drawer whenever the route changes
  useEffect(() => setSidebarOpen(false), [pathname]);

  const handleLogout = () => {
    logout();
    navigate(logoutRedirect, { replace: true });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Sidebar
        links={links}
        brand={brand}
        open={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        footer={sidebarFooter}
      />

      <div className="flex min-h-screen flex-col lg:pl-64">
        <Topbar
          title={title}
          user={user}
          onLogout={handleLogout}
          menuItems={userMenuItems}
          rightSlot={topbarRight}
          onMenuClick={() => setSidebarOpen(true)}
        />
        <main className="flex-1 p-4 sm:p-6">
          <div className="mx-auto max-w-6xl">
            <Outlet />
          </div>
        </main>
        <Footer text={footerText ?? `© ${new Date().getFullYear()} ${brand}`} links={footerLinks} />
      </div>
    </div>
  );
}