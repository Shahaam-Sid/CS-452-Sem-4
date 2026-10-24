import api from "./axios";

const ENDPOINTS = { login: "/auth/authenticate", register: "/auth/register" };

const extractToken = (data) => data?.token ?? data?.accessToken ?? data?.jwt ?? null;

export async function authenticate(credentials) {
  const { data } = await api.post(ENDPOINTS.login, credentials);
  const token = extractToken(data);
  if (!token) throw new Error("Login succeeded but the server sent no token.");
  return token;
}

export async function register(payload) {
  const { data } = await api.post(ENDPOINTS.register, payload);
  const token = extractToken(data);
  if (token) return token;

  // The register endpoint returned no token, so log in with the account just created.
  return authenticate({ identifier: payload.username, password: payload.password });
}