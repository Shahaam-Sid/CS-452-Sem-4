import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createSettlement, getGroupSettlements } from "../api/settlementsApi";
import { balanceKeys } from "./useBalances";

export const settlementKeys = {
  all: ["settlements"],
  group: (id) => ["settlements", "group", id],
};

export const useGroupSettlements = (groupId) =>
  useQuery({
    queryKey: settlementKeys.group(groupId),
    queryFn: () => getGroupSettlements(groupId),
    enabled: Boolean(groupId),
  });

  export function useCreateSettlement() {
    const qc = useQueryClient();
    return useMutation({
      mutationFn: createSettlement,
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: settlementKeys.all });
        qc.invalidateQueries({ queryKey: balanceKeys.all });
      },
    });
  }