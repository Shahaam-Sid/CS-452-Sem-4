import { useQuery } from "@tanstack/react-query";
import { getAllBalanceEntries, getGroupBalanceEntries } from "../api/balancesApi";
import { computeBalances } from "../utils/balances";
import { useMyGroups } from "./useGroups";
import useAuth from "./useAuth";

export const balanceKeys = {
  all: ["balances"],
  group: (groupId, userId) => ["balances", "group", groupId, userId],
  total: (userId) => ["balances", "total", userId],
};

// SRS FR-4.4: balances are recomputed on demand from current records, so never treated as fresh.
const onDemand = { staleTime: 0, refetchOnMount: "always" };

// group: { id, currency } (may be undefined while the group is loading)
export function useGroupBalances(group) {
  const { user } = useAuth();
  return useQuery({
    queryKey: balanceKeys.group(group?.id, user?.id),
    queryFn: async () => computeBalances(await getGroupBalanceEntries(group, user.id), user.id),
    enabled: Boolean(group?.id && user?.id),
    ...onDemand,
  });
}

export function useTotalBalances() {
  const { user } = useAuth();
  const groups = useMyGroups();
  const groupIds = (groups.data ?? []).map((g) => g.id);

  const balances = useQuery({
    queryKey: [...balanceKeys.total(user?.id), groupIds],
    queryFn: async () =>
      computeBalances(await getAllBalanceEntries(groups.data, user.id), user.id),
    enabled: Boolean(user?.id && groups.data),
    ...onDemand,
  });

  return {
    data: balances.data,
    isPending: groups.isPending || balances.isPending,
    isError: groups.isError || balances.isError,
    error: groups.error ?? balances.error,
    refetch: () => {
      groups.refetch();
      balances.refetch();
    },
  };
}