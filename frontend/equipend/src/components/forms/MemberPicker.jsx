import { useState } from "react";
import { Search, X } from "lucide-react";
import Input from "../ui/Input";
import Avatar from "../ui/Avatar";
import Spinner from "../ui/Spinner";
import useDebounce from "../../hooks/useDebounce";
import { useUserSearch } from "../../hooks/useUsers";
import { cn } from "../../utils/cn";

// Controlled: the parent owns `selected` (array of users) and receives changes via onChange.
// isExcluded(user) hides users that must not be offered (existing members...).
export default function MemberPicker({
  selected = [],
  onChange,
  isExcluded = () => false,
  multiple = true,
  label = "Members",
  placeholder = "Search by name, username or email",
  hint,
  className,
}) {
  const [term, setTerm] = useState("");
  const debounced = useDebounce(term.trim(), 300);
  const { data: results = [], isFetching, isError, error } = useUserSearch(debounced);

  const selectedIds = new Set(selected.map((u) => u.id));
  const options = results.filter((u) => !selectedIds.has(u.id) && !isExcluded(u));
  const searching = debounced.length >= 2;

  const pick = (user) => {
    onChange(multiple ? [...selected, user] : [user]);
    setTerm("");
  };
  const remove = (id) => onChange(selected.filter((u) => u.id !== id));

  return (
    <div className={cn("space-y-2", className)}>
      <Input
        label={label}
        hint={hint}
        value={term}
        placeholder={placeholder}
        leftIcon={<Search size={16} />}
        autoComplete="off"
        onChange={(e) => setTerm(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && e.preventDefault()} // don't submit a parent form
      />

      {searching && (
        <ul className="max-h-48 overflow-y-auto rounded-lg border border-gray-200 bg-white shadow-sm">
          {isFetching && (
            <li className="flex items-center gap-2 px-3 py-2 text-sm text-gray-500">
              <Spinner size="sm" /> Searching...
            </li>
          )}
          {isError && <li className="px-3 py-2 text-sm text-red-600">{error.userMessage}</li>}
          {!isFetching && !isError && options.length === 0 && (
            <li className="px-3 py-2 text-sm text-gray-500">No users found</li>
          )}
          {options.map((u) => (
            <li key={u.id}>
              <button
                type="button"
                onClick={() => pick(u)}
                className="flex w-full items-center gap-3 px-3 py-2 text-left hover:bg-gray-50"
              >
                <Avatar name={u.fullName} size="sm" />
                <span className="min-w-0">
                  <span className="block truncate text-sm font-medium text-gray-900">{u.fullName}</span>
                  <span className="block truncate text-xs text-gray-500">@{u.username}</span>
                </span>
              </button>
            </li>
          ))}
        </ul>
      )}

      {selected.length > 0 && (
        <div className="flex flex-wrap gap-2">
          {selected.map((u) => (
            <span
              key={u.id}
              className="inline-flex items-center gap-1.5 rounded-full bg-brand-50 py-1 pl-1 pr-2 text-sm text-brand-800"
            >
              <Avatar name={u.fullName} size="xs" />
              {u.fullName}
              <button
                type="button"
                onClick={() => remove(u.id)}
                aria-label={`Remove ${u.fullName}`}
                className="rounded-full p-0.5 hover:bg-brand-100"
              >
                <X size={14} />
              </button>
            </span>
          ))}
        </div>
      )}
    </div>
  );
}