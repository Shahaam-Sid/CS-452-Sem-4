import { Link } from "react-router-dom";
import Button from "../components/ui/Button";
import { APP_NAME } from "../config/app";

export default function DoorStep() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-6 bg-gradient-to-br from-brand-50 to-white p-6 text-center">
      <h1 className="text-4xl font-bold text-brand-700">{APP_NAME}</h1>
      <p className="max-w-md text-gray-600">Split expenses, not friendships.</p>
      <div className="flex gap-3">
        <Link to="/login"><Button>Log in</Button></Link>
        <Link to="/signup"><Button variant="outline">Sign up</Button></Link>
      </div>
    </div>
  );
}