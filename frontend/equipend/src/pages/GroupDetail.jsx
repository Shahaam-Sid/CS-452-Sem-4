import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ChevronLeft, HandCoins, Plus, UserPlus } from "lucide-react";
import Card from "../components/ui/Card";
import Badge from "../components/ui/Badge";
import Button from "../components/ui/Button";
import Alert from "../components/ui/Alert";
import Spinner from "../components/ui/Spinner";
import EmptyState from "../components/ui/EmptyState";
import MemberList from "../components/groups/MemberList";
import AddMemberModal from "../components/groups/AddMemberModal";
import ExpenseList from "../components/expenses/ExpenseList";
import AddExpenseModal from "../components/expenses/AddExpenseModal";
import SettlementList from "../components/settlements/SettlementList";
import SettleUpModal from "../components/settlements/SettleUpModal";
import BalanceList from "../components/balances/BalanceList";
import useAuth from "../hooks/useAuth";
import { useGroup, useGroupMembers } from "../hooks/useGroups";
import { useGroupExpenses } from "../hooks/useExpenses";
import { useGroupSettlements } from "../hooks/useSettlements";
import { useGroupBalances } from "../hooks/useBalances";
import { useUserDirectory } from "../hooks/useUsers";
import { formatCurrencyCode, formatDate } from "../utils/format";

export default function GroupDetail() {
  const { groupId } = useParams();
  const [addingMember, setAddingMember] = useState(false);
  const [addingExpense, setAddingExpense] = useState(false);
  const [settling, setSettling] = useState(false);
  const { user } = useAuth();
  const group = useGroup(groupId);
  const members = useGroupMembers(groupId);
  const expenses = useGroupExpenses(groupId);
  const settlements = useGroupSettlements(groupId);
  const balances = useGroupBalances(group.data); // waits until the group has loaded

  // everyone who can appear in this group's expenses, payments and balances is a member
  const directory = useUserDirectory((members.data ?? []).map((m) => m.userId));
  const memberName = (m) => directory.displayName(m.userId);

  const back = (
    <Link
      to="/dashboard/groups"
      className="mb-4 inline-flex items-center gap-1 text-sm text-gray-500 hover:text-brand-700"
    >
      <ChevronLeft size={16} /> Back to groups
    </Link>
  );

  if (group.isPending) {
    return (
      <div className="flex justify-center py-16 text-brand-600">
        <Spinner size="lg" />
      </div>
    );
  }

  if (group.isError) {
    const notFound = group.error.response?.status === 404;
    return (
      <div>
        {back}
        {notFound ? (
          <EmptyState
            title="Group not found"
            description="It may not exist, or you may not be a member of it."
          />
        ) : (
          <Alert variant="error" title="Could not load this group">
            <p>{group.error.userMessage}</p>
            <Button size="sm" variant="outline" className="mt-2" onClick={() => group.refetch()}>
              Try again
            </Button>
          </Alert>
        )}
      </div>
    );
  }

  const g = group.data;
  const itemProps = () => ({
    currency: g.currency,
    currentUserId: user?.id,
    getDisplayName: directory.displayName,
  });

  return (
    <div className="space-y-6">
      <div>
        {back}
        <Card>
          <div className="flex flex-wrap items-start justify-between gap-3">
            <div className="min-w-0">
              <div className="flex items-center gap-2">
                <h2 className="truncate text-xl font-semibold text-gray-900">{g.name}</h2>
                <Badge variant="success">{formatCurrencyCode(g.currency)}</Badge>
              </div>
              <p className="mt-1 text-sm text-gray-500">{g.description || "No description"}</p>
              {g.createdAt && (
                <p className="mt-2 text-xs text-gray-400">Created {formatDate(g.createdAt)}</p>
              )}
            </div>
            <Button leftIcon={<UserPlus size={16} />} onClick={() => setAddingMember(true)}>
              Add member
            </Button>
          </div>
        </Card>
      </div>

      <Card title="Your balances">
        {balances.isPending && (
          <div className="flex justify-center py-6 text-brand-600">
            <Spinner />
          </div>
        )}
        {balances.isError && (
          <Alert variant="error" title="Could not calculate balances">
            <p>{balances.error.userMessage}</p>
            <Button size="sm" variant="outline" className="mt-2" onClick={() => balances.refetch()}>
              Try again
            </Button>
          </Alert>
        )}
        {balances.data && (
          <BalanceList
            rows={balances.data}
            getDisplayName={directory.displayName}
            getAvatarName={directory.displayName}
            emptyText="No balances in this group yet. Expenses and payments that involve you will show up here."
          />
        )}
      </Card>

      <Card
        title="Expenses"
        actions={
          <Button
            size="sm"
            leftIcon={<Plus size={16} />}
            disabled={!members.data?.length}
            onClick={() => setAddingExpense(true)}
          >
            Add expense
          </Button>
        }
      >
        {expenses.isPending && (
          <div className="flex justify-center py-6 text-brand-600">
            <Spinner />
          </div>
        )}
        {expenses.isError && (
          <Alert variant="error" title="Could not load expenses">
            {expenses.error.userMessage}
          </Alert>
        )}
        {expenses.data && (
          <ExpenseList
            expenses={expenses.data}
            getItemProps={itemProps}
            emptyText="No expenses in this group yet."
          />
        )}
      </Card>

      <Card
        title="Settlements"
        actions={
          <Button
            size="sm"
            variant="outline"
            leftIcon={<HandCoins size={16} />}
            disabled={(members.data?.length ?? 0) < 2}
            onClick={() => setSettling(true)}
          >
            Settle up
          </Button>
        }
      >
        {settlements.isPending && (
          <div className="flex justify-center py-6 text-brand-600">
            <Spinner />
          </div>
        )}
        {settlements.isError && (
          <Alert variant="error" title="Could not load settlements">
            {settlements.error.userMessage}
          </Alert>
        )}
        {settlements.data && (
          <SettlementList
            settlements={settlements.data}
            getItemProps={itemProps}
            emptyText="No payments recorded in this group yet."
          />
        )}
      </Card>

      <Card title={`Members${members.data ? ` (${members.data.length})` : ""}`}>
        {members.isPending && (
          <div className="flex justify-center py-6 text-brand-600">
            <Spinner />
          </div>
        )}
        {members.isError && <Alert variant="error">{members.error.userMessage}</Alert>}
        {members.data && (
          <MemberList
            members={members.data}
            currentUserId={user?.id}
            getDisplayName={memberName}
            getUsername={(m) => directory.username(m.userId)}
          />
        )}
      </Card>

      <AddMemberModal
        open={addingMember}
        onClose={() => setAddingMember(false)}
        groupId={groupId}
        existingMemberIds={(members.data ?? []).map((m) => m.userId)}
      />

      {members.data && (
        <>
          <AddExpenseModal
            open={addingExpense}
            onClose={() => setAddingExpense(false)}
            group={g}
            members={members.data}
            getDisplayName={memberName}
          />
          <SettleUpModal
            open={settling}
            onClose={() => setSettling(false)}
            group={g}
            members={members.data}
            getDisplayName={memberName}
          />
        </>
      )}
    </div>
  );
}