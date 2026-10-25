import { useQueries, useQuery } from "@tanstack/react-query";
import { getUser, searchUsers } from "../api/usersApi";
import { shortId } from "../utils/format";

export const userKeys = {
  detail: (id) => ["users", "detail", id],
  search: (term) => ["users", "search", term],
};

export const useUserSearch = (term) =>
  useQuery({
    queryKey: userKeys.search(term),
    queryFn: () => searchUsers(term),
    enabled: term.trim().length >= 2,
    staleTime: 60_000,
  });

// Looks up every distinct id once (cached for 10 minutes) and returns lookup helpers.
// While a name is loading, or if the lookup fails, the short id is shown as a fallback.
export function useUserDirectory(ids = []) {
  const unique = [...new Set(ids.filter(Boolean))];

  const results = useQueries({
    queries: unique.map((id) => ({
      queryKey: userKeys.detail(id),
      queryFn: () => getUser(id),
      staleTime: 10 * 60_000,
    })),
  });

  const byId = new Map();
  unique.forEach((id, i) => {
    if (results[i]?.data) byId.set(id, results[i].data);
  });

  return {
    getUser: (id) => byId.get(id) ?? null,
    displayName: (id) => byId.get(id)?.fullName ?? `Member ${shortId(id)}`,
    username: (id) => byId.get(id)?.username ?? null,
  };
}