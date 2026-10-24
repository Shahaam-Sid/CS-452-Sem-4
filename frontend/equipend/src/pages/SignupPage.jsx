import { Link, useLocation } from "react-router-dom";
import AuthLayout from "../components/layouts/AuthLayout";
import RegisterForm from "../components/forms/RegisterForm";
import useAuth from "../hooks/useAuth";
import { register as registerUser } from "../api/authApi";
import { APP_NAME } from "../config/app";

export default function SignupPage() {
  const { login } = useAuth();
  const location = useLocation();

  const handleRegister = async (payload) => {
    const response = await registerUser(payload);
    login(response); // auto-login; GuestOnlyRoute then redirects
  };

  return (
    <AuthLayout brand={APP_NAME} tagline="Create your account">
      <RegisterForm
        onSubmit={handleRegister}
        footer={
          <p className="text-center text-sm text-gray-500">
            Already have an account?{" "}
            <Link to="/login" state={location.state} className="font-medium text-brand-700 hover:underline">
              Log in
            </Link>
          </p>
        }
      />
    </AuthLayout>
  );
}