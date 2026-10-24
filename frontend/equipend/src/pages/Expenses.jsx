import { useMemo, useState } from "react";
import Card from "../components/ui/Card";
import Alert from "../components/ui/Alert";
import Button from "../components/ui/Button";
import Spinner from "../components/ui/Spinner";
import Tabs from "../components/ui/Tabs";
import ExpenseList from "../components/expenses/ExpenseList";
import useAuth from "../hooks/useAuth";
import { useMyGroups } from "../hooks/useGroups";
import { useMyShares, usePaidExpenses } from "../hooks/useExpenses";

export default function Expenses() {
  const { user } = useAuth();
  const [tab, setTab] = useState("shares");
  const shares = useMyShares();
  const paid = usePaidExpenses();
  const groups = useMyGroups();

  // group names and currencies come from the groups list: no extra requests
  const groupById = useMemo(
    () => new Map((groups.data ?? []).map((g) => [g.id, g])),
    [groups.data]
  );

  const active = tab === "shares" ? shares : paid;

  const getItemProps = (e) => {
    const g = groupById.get(e.groupId);
    return {
      currency: g?.currency,
      groupName: g?.name,
      currentUserId: user?.id,
      share: e.myShare, // undefined on the "Paid by me" tab
      to: `/dashboard/groups/${e.groupId}`,
    };
  };

  return (
    <div className="space-y-4">
      <Tabs
        value={tab}
        onChange={setTab}
        tabs={[
          { id: "shares", label: "My shares", count: shares.data?.length },
          { id: "paid", label: "Paid by me", count: paid.data?.length },
        ]}
      />

      <Card>
        {active.isPending && (
          <div className="flex justify-center py-10 text-brand-600">
            <Spinner size="lg" />
          </div>
        )}

        {active.isError && (
          <Alert variant="error" title="Could not load expenses">
            <p>{active.error.userMessage}</p>
            <Button size="sm" variant="outline" className="mt-2" onClick={() => active.refetch()}>
              Try again
            </Button>
          </Alert>
        )}

        {active.data && (
          <ExpenseList
            expenses={active.data}
            getItemProps={getItemProps}
            emptyText={
              tab === "shares"
                ? "You have no shares yet. Expenses that include you will appear here."
                : "You have not paid for any expenses yet."
            }
          />
        )}
      </Card>
    </div>
  );
}