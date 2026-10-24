import api from "./axios";

// Paths come from your controllers (relative to the /api/v1 base URL).
const ENDPOINTS = {
  create: "/expense",
  expense: (id) => `/expense/${id}`,
  byGroup: (groupId) => `/expense/group/${groupId}`,
  byPayer: (userId) => `/expense/payer/${userId}`,
  splitsByUser: (userId) => `/splits/user/${userId}`,
  splitsByExpense: (expenseId) => `/splits/expense/${expenseId}`,
};

const unwrapList = (data) => (Array.isArray(data) ? data : data?.content ?? []);
const newestFirst = (a, b) => new Date(b.createdAt) - new Date(a.createdAt);

// Backend shape -> UI shape (ExpenseResponseDTO / ExpenseSplitResponseDTO).
export function normalizeExpense(raw = {}) {
  return {
    id: raw.expenseID ?? raw.id ?? null,
    title: raw.title,
    amount: Number(raw.amount),
    groupId: raw.groupID ?? null,
    createdBy: raw.createdBy ?? null,
    paidBy: raw.paidBy ?? null,
    createdAt: raw.createdAt ?? null,
    splitType: raw.splitType ?? null,
  };
}

export function normalizeSplit(raw = {}) {
  return {
    userId: raw.user ?? null,
    expenseId: raw.expense ?? null,
    shareAmount: Number(raw.shareAmount),
  };
}

export async function getExpense(id) {
  const { data } = await api.get(ENDPOINTS.expense(id));
  return normalizeExpense(data);
}

export async function getGroupExpenses(groupId) {
  const { data } = await api.get(ENDPOINTS.byGroup(groupId));
  return unwrapList(data).map(normalizeExpense).sort(newestFirst);
}

export async function getExpensesPaidBy(userId) {
  const { data } = await api.get(ENDPOINTS.byPayer(userId));
  return unwrapList(data).map(normalizeExpense).sort(newestFirst);
}

export async function getExpenseSplits(expenseId) {
  const { data } = await api.get(ENDPOINTS.splitsByExpense(expenseId));
  return unwrapList(data).map(normalizeSplit);
}

// Every split the user owes, joined with its expense (N+1 until the backend has a join query).
export async function getMyShares(userId) {
  const { data } = await api.get(ENDPOINTS.splitsByUser(userId));
  const splits = unwrapList(data).map(normalizeSplit);
  const expenses = await Promise.all(
    splits.map(async (s) => ({ ...(await getExpense(s.expenseId)), myShare: s.shareAmount }))
  );
  return expenses.sort(newestFirst);
}

// payload: { title, amount, groupID, createdBy, paidBy, splitType, participants, participantShare }
export async function createExpense(payload) {
  const { data } = await api.post(ENDPOINTS.create, payload);
  return normalizeExpense(data);
}