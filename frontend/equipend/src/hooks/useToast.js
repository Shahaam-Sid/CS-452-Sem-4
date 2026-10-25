import { useContext } from "react";
import { ToastContext } from "../components/context/toast-context";

export default function useToast() {
  const ctx = useContext(ToastContext);
  if (!ctx) throw new Error("useToast must be used inside <ToastProvider>");
  return ctx;
}