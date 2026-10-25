import { useCallback, useMemo, useRef, useState } from "react";
import { ToastContext } from "./toast-context";
import Toast from "../ui/Toast";
import { cn } from "../../utils/cn";

const positions = {
  "bottom-right": "bottom-4 right-4 items-end",
  "bottom-left": "bottom-4 left-4 items-start",
  "top-right": "top-20 right-4 items-end",
  "top-left": "top-20 left-4 items-start",
};

export function ToastProvider({
  children,
  position = "bottom-right",
  defaultDuration = 4000, // ms; 0 keeps a toast until dismissed
  max = 4, // most toasts on screen at once
}) {
  const [toasts, setToasts] = useState([]);
  const counter = useRef(0);

  const dismiss = useCallback((id) => setToasts((list) => list.filter((t) => t.id !== id)), []);

  const show = useCallback(
    (message, { variant = "info", duration = defaultDuration } = {}) => {
      const id = ++counter.current;
      setToasts((list) => [...list, { id, message, variant }].slice(-max));
      if (duration > 0) setTimeout(() => dismiss(id), duration);
      return id;
    },
    [defaultDuration, max, dismiss]
  );

  const api = useMemo(
    () => ({
      show,
      dismiss,
      success: (message, options) => show(message, { ...options, variant: "success" }),
      info: (message, options) => show(message, { ...options, variant: "info" }),
      warning: (message, options) => show(message, { ...options, variant: "warning" }),
      error: (message, options) => show(message, { duration: 6000, ...options, variant: "error" }),
    }),
    [show, dismiss]
  );

  return (
    <ToastContext.Provider value={api}>
      {children}
      <div
        aria-live="polite"
        className={cn("pointer-events-none fixed z-[60] flex flex-col gap-2", positions[position])}
      >
        {toasts.map((t) => (
          <Toast key={t.id} variant={t.variant} message={t.message} onClose={() => dismiss(t.id)} />
        ))}
      </div>
    </ToastContext.Provider>
  );
}