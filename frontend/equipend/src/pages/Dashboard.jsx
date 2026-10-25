import { useMemo } from "react";
import Card from "../components/ui/Card";
import Alert from "../components/ui/Alert";
import Button from "../components/ui/Button";
import Spinner from "../components/ui/Spinner";
import BalanceList from "../components/balances/BalanceList";
import useAuth from "../hooks/useAuth";
import { useMyGroups } from "../hooks/useGroups";
import { useTotalBalances } from "../hooks/useBalances";
import { useUserDirectory } from "../hooks/useUsers";
import { summarizeBalances } from "../utils/balances";
import { formatCents, formatCurrencyCode } from "../utils/format";
import { cn } from "../utils/cn";

function SummaryCard({ label, cents, currency, tone }) {
  return (
    <Card padding="sm">
      <p className="text-xs text-gray-500">{label}</p>
      <p className={cn("mt-1 text-xl font-semibold", tone === "owe" ? "text-red-600" : "text-brand-700")}>
        {formatCents(cents, currency)}
      </p>
    </Card>
  );
}

export default function Dashboard() {
  const { user } = useAuth();
  const groups = useMyGroups();
  const balances = useTotalBalances();

  // names for the people you owe or are owed by
  const directory = useUserDirectory((balances.data ?? []).map((r) => r.userId));

  const groupNames = useMemo(
    () => new Map((groups.data ?? []).map((g) => [g.id, g.name])),
    [groups.data]
  );
  const summary = useMemo(() => summarizeBalances(balances.data ?? []), [balances.data]);

  return (
    <div className="space-y-6">
      <Card title={`Welcome, ${user?.name}`} subtitle="Your balances across all groups." />

      {balances.isPending && (
        <div className="flex justify-center py-10 text-brand-600">
          <Spinner size="lg" />
        </div>
      )}

      {balances.isError && (
        <Alert variant="error" title="Could not calculate your balances">
          <p>{balances.error.userMessage}</p>
          <Button size="sm" variant="outline" className="mt-2" onClick={() => balances.refetch()}>
            Try again
          </Button>
        </Alert>
      )}

      {balances.data && (
        <>
          {summary.map((s) => (
            <div key={s.currency} className="grid gap-4 sm:grid-cols-2">
              <SummaryCard
                label={`You owe (${formatCurrencyCode(s.currency)})`}
                cents={s.youOwe}
                currency={s.currency}
                tone="owe"
              />
              <SummaryCard
                label={`You are owed (${formatCurrencyCode(s.currency)})`}
                cents={s.theyOwe}
                currency={s.currency}
                tone="owed"
              />
            </div>
          ))}

          <BalanceList
            rows={balances.data}
            groupNames={groupNames}
            getDisplayName={directory.displayName}
            getAvatarName={directory.displayName}
            emptyText="No balances yet. Expenses and payments that involve you will show up here."
          />
        </>
      )}
    </div>
  );
}