import { useCallback, useEffect, useMemo, useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { jwtDecode } from "jwt-decode";
import { AuthContext } from "./auth-context";
import { TOKEN_KEY, UNAUTHORIZED_EVENT } from "../../api/axios";

const USER_KEY = "user";
const MAX_TIMEOUT = 2 ** 31 - 1; // setTimeout overflows above this

// Maps the login/signup response to the user object used across the app.
const mapAuthResponse = (r) => ({
  id: r.userID ?? r.userId ?? null,
  username: r.username,
  fullName: r.fullName,
  email: r.email ?? null,
  name: r.fullName ?? r.username, // shown in the Topbar
});

const clearStorage = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
};

const isExpired = (claims) => claims.exp && claims.exp * 1000 <= Date.now();

// Runs once, synchronously, so there is no "loading" flash on refresh.
function readStoredAuth() {
  try {
    const token = localStorage.getItem(TOKEN_KEY);
    const user = JSON.parse(localStorage.getItem(USER_KEY) ?? "null");
    if (!token || !user?.id) {
      clearStorage();
      return { token: null, user: null };
    }
    const claims = jwtDecode(token);
    if (isExpired(claims)) {
      clearStorage();
      return { token: null, user: null };
    }
    return { token, user, exp: claims.exp };
  } catch {
    clearStorage();
    return { token: null, user: null };
  }
}

export function AuthProvider({ children }) {
  const queryClient = useQueryClient();
  const [auth, setAuth] = useState(readStoredAuth);

  const logout = useCallback(() => {
    clearStorage();
    queryClient.clear(); // never show one user's cached data to the next
    setAuth({ token: null, user: null });
  }, [queryClient]);

  // `response` is the login/signup DTO: { userID, username, fullName, email, token }
  const login = useCallback((response) => {
    if (!response?.token) throw new Error("The server did not send a token.");
    const user = mapAuthResponse(response);
    if (!user.id) throw new Error("The server did not send a user id.");
    const claims = jwtDecode(response.token);
    if (isExpired(claims)) throw new Error("Token has already expired");

    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    setAuth({ token: response.token, user, exp: claims.exp });
  }, []);

  // schedule an auto-logout for the moment the token expires
  useEffect(() => {
    if (!auth.exp) return;
    const ms = Math.min(Math.max(0, auth.exp * 1000 - Date.now()), MAX_TIMEOUT);
    const id = setTimeout(logout, ms);
    return () => clearTimeout(id);
  }, [auth.exp, logout]);

  // the API layer fires this event when the server rejects our token
  useEffect(() => {
    window.addEventListener(UNAUTHORIZED_EVENT, logout);
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, logout);
  }, [logout]);

  const value = useMemo(
    () => ({ user: auth.user, token: auth.token, isAuthenticated: !!auth.token, login, logout }),
    [auth.user, auth.token, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}