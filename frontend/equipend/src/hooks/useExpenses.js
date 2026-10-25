import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createExpense, getExpensesPaidBy, getGroupExpenses, getMyShares } from "../api/expensesApi";
import useAuth from "./useAuth";
import { balanceKeys } from "./useBalances";

export const expenseKeys = {
  all: ["expenses"],
  group: (id) => ["expenses", "group", id],
  shares: (userId) => ["expenses", "shares", userId],
  paid: (userId) => ["expenses", "paid", userId],
};

export const useGroupExpenses = (groupId) =>
  useQuery({
    queryKey: expenseKeys.group(groupId),
    queryFn: () => getGroupExpenses(groupId),
    enabled: Boolean(groupId),
  });

export function useMyShares() {
  const { user } = useAuth();
  return useQuery({
    queryKey: expenseKeys.shares(user?.id),
    queryFn: () => getMyShares(user.id),
    enabled: Boolean(user?.id),
  });
}

export function usePaidExpenses() {
  const { user } = useAuth();
  return useQuery({
    queryKey: expenseKeys.paid(user?.id),
    queryFn: () => getExpensesPaidBy(user.id),
    enabled: Boolean(user?.id),
  });
}

export function useCreateExpense() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: createExpense,
    onSuccess: () => {
      // "expenses" is a prefix: refreshes the group list and both dedicated-page tabs
      qc.invalidateQueries({ queryKey: expenseKeys.all });
      qc.invalidateQueries({ queryKey: balanceKeys.all });
    },
  });
}