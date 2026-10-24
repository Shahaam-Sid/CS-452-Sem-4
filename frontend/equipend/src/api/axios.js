import axios from "axios";

export const TOKEN_KEY = "token";
export const UNAUTHORIZED_EVENT = "auth:unauthorized";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api",
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

export function getErrorMessage(error, fallback = "Something went wrong. Please try again.") {
  if (error.code === "ECONNABORTED") return "The request timed out. Please try again.";
  if (!error.response) return "Cannot reach the server. Check your connection.";

  const data = error.response.data;
  if (typeof data === "string" && data.trim()) return data;

  if (data?.errors && typeof data.errors === "object") {
    const messages = Array.isArray(data.errors)
      ? data.errors.map((e) => e.defaultMessage ?? e.message ?? String(e))
      : Object.values(data.errors).map(String);
    if (messages.length) return messages.join(". ");
  }

  return data?.message || data?.error || fallback;
}

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const sentToken = Boolean(error.config?.headers?.Authorization);

    if (status === 401 && sentToken) {
      window.dispatchEvent(new Event(UNAUTHORIZED_EVENT));
    }

    error.userMessage = getErrorMessage(error);
    return Promise.reject(error);
  }
);

export default api;