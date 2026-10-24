import api from "./axios";

// Paths come from your controllers (relative to the /api/v1 base URL).
const ENDPOINTS = {
  createGroup: "/group",
  group: (id) => `/group/${id}`,
  membershipsOfUser: (userId) => `/member/user/${userId}`,
  membersOfGroup: (groupId) => `/member/group/${groupId}`,
  addMember: "/member",
  // ASSUMPTION: the user search controller wasn't in what you sent. Change to the real path.
  searchUsers: "/user/search",
};

// Spring returns either a plain array or a Page object with a `content` array.
const unwrapList = (data) => (Array.isArray(data) ? data : data?.content ?? []);

// A reference may arrive as a plain UUID or as a nested object; return the id either way.
const idOf = (v) => (v && typeof v === "object" ? v.id ?? v.groupID ?? v.userID ?? null : v ?? null);

// Backend shape -> UI shape lives ONLY in these normalizers.
export function normalizeGroup(raw = {}) {
  return {
    id: raw.groupID ?? raw.id ?? null,
    name: raw.name,
    description: raw.description ?? "",
    currency: raw.currency,
    createdBy: idOf(raw.createdBy),
    createdAt: raw.createdAt ?? null,
  };
}

export function normalizeMembership(raw = {}) {
  return { userId: idOf(raw.user), groupId: idOf(raw.group), joinedAt: raw.joinedAt ?? null };
}

export function normalizeUser(raw = {}) {
  return {
    id: raw.userID ?? raw.id ?? null,
    fullName: raw.fullName ?? raw.username ?? "Unknown",
    username: raw.username ?? null,
    email: raw.email ?? null,
  };
}

export async function getGroup(id) {
  const { data } = await api.get(ENDPOINTS.group(id));
  return normalizeGroup(data);
}

// payload: { name, description, currency, createdBy }
export async function createGroup(payload) {
  const { data } = await api.post(ENDPOINTS.createGroup, payload);
  return normalizeGroup(data);
}

export async function getGroupMembers(groupId) {
  const { data } = await api.get(ENDPOINTS.membersOfGroup(groupId));
  return unwrapList(data).map(normalizeMembership);
}

// "My groups": memberships first, then one request per group (N+1 until the backend has a join query).
export async function getMyGroups(userId) {
  const { data } = await api.get(ENDPOINTS.membershipsOfUser(userId));
  const memberships = unwrapList(data).map(normalizeMembership);
  const groups = await Promise.all(
    memberships.map(async (m) => ({ ...(await getGroup(m.groupId)), joinedAt: m.joinedAt }))
  );
  return groups.sort((a, b) => new Date(b.joinedAt) - new Date(a.joinedAt));
}

export async function addGroupMember(groupId, userId) {
  const { data } = await api.post(ENDPOINTS.addMember, { user: userId, group: groupId });
  return normalizeMembership(data);
}

export async function searchUsers(query) {
  const { data } = await api.get(ENDPOINTS.searchUsers, { params: { query } });
  return unwrapList(data).map(normalizeUser);
}