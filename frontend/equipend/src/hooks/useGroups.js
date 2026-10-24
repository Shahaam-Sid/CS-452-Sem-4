import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  addGroupMember,
  createGroup,
  getGroup,
  getGroupMembers,
  getMyGroups,
  searchUsers,
} from "../api/groupsApi";
import useAuth from "./useAuth";

export const groupKeys = {
  all: ["groups"],
  mine: (userId) => ["groups", "mine", userId],
  detail: (id) => ["groups", "detail", id],
  members: (id) => ["groups", "detail", id, "members"],
  userSearch: (term) => ["users", "search", term],
};

export function useMyGroups() {
  const { user } = useAuth();
  return useQuery({
    queryKey: groupKeys.mine(user?.id),
    queryFn: () => getMyGroups(user.id),
    enabled: Boolean(user?.id),
  });
}

export const useGroup = (groupId) =>
  useQuery({
    queryKey: groupKeys.detail(groupId),
    queryFn: () => getGroup(groupId),
    enabled: Boolean(groupId),
  });

export const useGroupMembers = (groupId) =>
  useQuery({
    queryKey: groupKeys.members(groupId),
    queryFn: () => getGroupMembers(groupId),
    enabled: Boolean(groupId),
  });

export function useCreateGroup() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: createGroup,
    onSuccess: () => qc.invalidateQueries({ queryKey: groupKeys.all }),
  });
}

export function useAddMember(groupId) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (userId) => addGroupMember(groupId, userId),
    // "groups" is a prefix, so this refreshes the list, the detail and the members
    onSuccess: () => qc.invalidateQueries({ queryKey: groupKeys.all }),
  });
}

export const useUserSearch = (term) =>
  useQuery({
    queryKey: groupKeys.userSearch(term),
    queryFn: () => searchUsers(term),
    enabled: term.trim().length >= 2,
    staleTime: 60_000,
  });