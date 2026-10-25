import api from "./axios";

// Contract: GET /user/search?query= -> [{ userID, username, fullName }]
//           GET /user/{id}          -> { userID, username, fullName }
const ENDPOINTS = {
  search: "/user/search",
  byId: (id) => `/user/${id}`,
};

const unwrapList = (data) => (Array.isArray(data) ? data : data?.content ?? []);

// Backend shape -> UI shape. The id is carried in the data but never displayed.
export function normalizeUser(raw = {}) {
  return {
    id: raw.userID ?? raw.userId ?? raw.id ?? null,
    fullName: raw.fullName ?? raw.username ?? "Unknown",
    username: raw.username ?? null,
  };
}

export async function searchUsers(query) {
  const { data } = await api.get(ENDPOINTS.search, { params: { query } });
  return unwrapList(data).map(normalizeUser);
}

export async function getUser(id) {
  const { data } = await api.get(ENDPOINTS.byId(id));
  return normalizeUser(data);
}