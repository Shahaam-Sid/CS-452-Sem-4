import api from "./axios";

// Paths come from your controller (relative to the /api/v1 base URL).
const ENDPOINTS = {
  create: "/settlement",
  byGroup: (groupId) => `/settlement/group/${groupId}`,
};

const unwrapList = (data) => (Array.isArray(data) ? data : data?.content ?? []);
const newestFirst = (a, b) => new Date(b.timestamp) - new Date(a.timestamp);

// Backend shape -> UI shape (SettlementResponseDTO).
export function normalizeSettlement(raw = {}) {
  return {
    id: raw.settlementID ?? raw.id ?? null,
    note: raw.note ?? "",
    amount: Number(raw.amount),
    timestamp: raw.timestamp ?? null,
    paidTo: raw.paidTo ?? null,
    paidBy: raw.paidBy ?? null,
    groupId: raw.group ?? null,
  };
}

export async function getGroupSettlements(groupId) {
  const { data } = await api.get(ENDPOINTS.byGroup(groupId));
  return unwrapList(data).map(normalizeSettlement).sort(newestFirst);
}

// payload: { note, amount, paidTo, paidBy, group }
export async function createSettlement(payload) {
  const { data } = await api.post(ENDPOINTS.create, payload);
  return normalizeSettlement(data);
}