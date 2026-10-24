import { useCallback, useEffect, useMemo, useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { jwtDecode } from "jwt-decode";
import { AuthContext } from "./auth-context";
import { TOKEN_KEY, UNAUTHORIZED_EVENT } from "../../api/axios";

const MAX_TIMEOUT = 2 ** 31 - 1; // setTimeout overflows above this

// The ONE place that maps JWT claims to the user object. Edit to match your backend.
const mapClaimsToUser = (claims) => ({
  id: claims.userId ?? null,
  username: claims.sub,
  email: claims.email ?? null,
  name: claims.name ?? claims.sub,
});

const isExpired = (claims) => claims.exp && claims.exp * 1000 <= Date.now();

// Runs once, synchronously, so there is no "loading" flash on refresh.
function readStoredAuth() {
  try {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) return { token: null, user: null };
    const claims = jwtDecode(token);
    if (isExpired(claims)) {
      localStorage.removeItem(TOKEN_KEY);
      return { token: null, user: null };
    }
    return { token, user: mapClaimsToUser(claims), exp: claims.exp };
  } catch {
    localStorage.removeItem(TOKEN_KEY);
    return { token: null, user: null };
  }
}

export function AuthProvider({ children }) {
  const queryClient = useQueryClient();
  const [auth, setAuth] = useState(readStoredAuth);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    queryClient.clear(); // never show one user's cached data to the next
    setAuth({ token: null, user: null });
  }, [queryClient]);

  const login = useCallback((token) => {
    const claims = jwtDecode(token); // throws if the token is malformed
    if (isExpired(claims)) throw new Error("Token has already expired");
    localStorage.setItem(TOKEN_KEY, token);
    setAuth({ token, user: mapClaimsToUser(claims), exp: claims.exp });
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