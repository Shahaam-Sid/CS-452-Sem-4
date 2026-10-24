import { useState } from "react";
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
  const { pathname } = useLocation();
  const matches = useMatches();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  // The drawer remembers WHICH path it was opened on. When the route changes the two
  // stop matching, so it counts as closed. No effect needed.
  const [drawerPath, setDrawerPath] = useState(null);
  const sidebarOpen = drawerPath === pathname;

  // title comes from the deepest matched route that defines handle.title
  const title = [...matches].reverse().find((m) => m.handle?.title)?.handle.title ?? "";

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
        onClose={() => setDrawerPath(null)}
        footer={sidebarFooter}
      />

      <div className="flex min-h-screen flex-col lg:pl-64">
        <Topbar
          title={title}
          user={user}
          onLogout={handleLogout}
          menuItems={userMenuItems}
          rightSlot={topbarRight}
          onMenuClick={() => setDrawerPath(pathname)}
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