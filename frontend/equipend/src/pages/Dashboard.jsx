import Card from "../components/ui/Card";
import useAuth from "../hooks/useAuth";

export default function Dashboard() {
  const { user } = useAuth();
  return <Card title={`Welcome, ${user?.name}`} subtitle="Dashboard content goes here." />;
}