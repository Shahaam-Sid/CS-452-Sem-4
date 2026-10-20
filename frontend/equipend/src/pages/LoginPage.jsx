import { useNavigate } from "react-router-dom";
import AuthLayout from "../components/layout/AuthLayout";
import Button from "../components/ui/Button";
import useAuth from "../hooks/useAuth";
import { APP_NAME } from "../config/app";

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const devLogin = () => {
    const payload = { sub: "demo", email: "demo@example.com", exp: Math.floor(Date.now() / 1000) + 3600 };
    login(`x.${btoa(JSON.stringify(payload))}.x`);
    navigate("/dashboard", { replace: true });
  };

  return (
    <AuthLayout brand={APP_NAME} tagline="Log in to continue">
      <p className="mb-4 text-sm text-gray-500">Login form coming in the forms batch.</p>
      <Button fullWidth onClick={devLogin}>Dev: fake login</Button>
    </AuthLayout>
  );
}