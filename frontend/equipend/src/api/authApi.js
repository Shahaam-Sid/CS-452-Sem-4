import api from "./axios";

// ASSUMPTION: auth endpoint paths (I haven't seen your auth controller).
// Both endpoints return { userID, username, fullName, email, token }.
const ENDPOINTS = { login: "/auth/authenticate", register: "/auth/register" };

export async function authenticate(credentials) {
  const { data } = await api.post(ENDPOINTS.login, credentials);
  return data;
}

export async function register(payload) {
  const { data } = await api.post(ENDPOINTS.register, payload);
  return data;
}