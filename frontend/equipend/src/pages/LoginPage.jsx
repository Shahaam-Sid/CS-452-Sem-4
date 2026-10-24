import { Link, useLocation } from "react-router-dom";
import AuthLayout from "../components/layouts/AuthLayout";
import LoginForm from "../components/forms/LoginForm";
import Alert from "../components/ui/Alert";
import useAuth from "../hooks/useAuth";
import { authenticate } from "../api/authApi";
import { APP_NAME } from "../config/app";

export default function LoginPage() {
  const { login } = useAuth();
  const location = useLocation();

  const handleLogin = async (values) => {
    const token = await authenticate(values);
    login(token); // GuestOnlyRoute redirects as soon as auth state flips
  };

  return (
    <AuthLayout brand={APP_NAME} tagline="Log in to continue">
      {location.state?.from && (
        <Alert variant="info" className="mb-4">Please log in to continue.</Alert>
      )}
      <LoginForm
        onSubmit={handleLogin}
        footer={
          <p className="text-center text-sm text-gray-500">
            No account?{" "}
            <Link to="/signup" state={location.state} className="font-medium text-brand-700 hover:underline">
              Sign up
            </Link>
          </p>
        }
      />
    </AuthLayout>
  );
}