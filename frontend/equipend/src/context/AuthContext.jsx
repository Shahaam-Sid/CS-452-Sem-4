import { createContext, useCallback, useEffect, useMemo, useState } from "react";
import { jwtDecode } from "jwt-decode";

const TOKEN_KEY = "token";
const MAX_TIMEOUT = 2 ** 31 - 1; // setTimeout overflows above this

export const AuthContext = createContext(null);

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
  const [auth, setAuth] = useState(readStoredAuth);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    setAuth({ token: null, user: null });
  }, []);

  const login = useCallback((token) => {
    const claims = jwtDecode(token); // throws if the token is malformed
    localStorage.setItem(TOKEN_KEY, token);
    setAuth({ token, user: mapClaimsToUser(claims), exp: claims.exp });
  }, []);

  // auto-logout the moment the token expires
  useEffect(() => {
    if (!auth.exp) return;
    const ms = auth.exp * 1000 - Date.now();
    if (ms <= 0) return logout();
    const id = setTimeout(logout, Math.min(ms, MAX_TIMEOUT));
    return () => clearTimeout(id);
  }, [auth.exp, logout]);

  const value = useMemo(
    () => ({ user: auth.user, token: auth.token, isAuthenticated: !!auth.token, login, logout }),
    [auth.user, auth.token, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}