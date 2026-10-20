import AuthLayout from "../components/layout/AuthLayout";
import { APP_NAME } from "../config/app";

export default function SignupPage() {
  return (
    <AuthLayout brand={APP_NAME} tagline="Create your account">
      <p className="text-sm text-gray-500">Signup form coming in the forms batch.</p>
    </AuthLayout>
  );
}