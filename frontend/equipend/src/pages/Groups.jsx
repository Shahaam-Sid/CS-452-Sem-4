import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Plus, Users } from "lucide-react";
import Button from "../components/ui/Button";
import Modal from "../components/ui/Modal";
import Alert from "../components/ui/Alert";
import Spinner from "../components/ui/Spinner";
import EmptyState from "../components/ui/EmptyState";
import GroupCard from "../components/groups/GroupCard";
import CreateGroupForm from "../components/forms/CreateGroupForm";
import useAuth from "../hooks/useAuth";
import { useCreateGroup, useMyGroups } from "../hooks/useGroups";
import useToast from "../hooks/useToast";

export default function Groups() {
  const [creating, setCreating] = useState(false);
  const navigate = useNavigate();
  const { user } = useAuth();
  const { data: groups, isPending, isError, error, refetch } = useMyGroups();
  const createGroup = useCreateGroup();
  const toast = useToast();

  const handleCreate = async (values) => {
    // errors surface inside the form; the backend adds the creator as the first member
    const group = await createGroup.mutateAsync({ ...values, createdBy: user.id });
    toast.success("Group created");
    setCreating(false);
    if (group.id) navigate(`/dashboard/groups/${group.id}`);
  };

  const newGroupButton = (
    <Button leftIcon={<Plus size={16} />} onClick={() => setCreating(true)}>
      New group
    </Button>
  );

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-3">
        <p className="text-sm text-gray-500">Groups you belong to.</p>
        {groups?.length > 0 && newGroupButton}
      </div>

      {isPending && (
        <div className="flex justify-center py-16 text-brand-600">
          <Spinner size="lg" />
        </div>
      )}

      {isError && (
        <Alert variant="error" title="Could not load your groups">
          <p>{error.userMessage}</p>
          <Button size="sm" variant="outline" className="mt-2" onClick={() => refetch()}>
            Try again
          </Button>
        </Alert>
      )}

      {groups?.length === 0 && (
        <EmptyState
          icon={<Users size={24} />}
          title="No groups yet"
          description="Create a group to start splitting expenses with friends, flatmates or family."
          action={newGroupButton}
        />
      )}

      {groups?.length > 0 && (
        <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
          {groups.map((g) => (
            <GroupCard key={g.id} group={g} to={`/dashboard/groups/${g.id}`} />
          ))}
        </div>
      )}

      <Modal open={creating} onClose={() => setCreating(false)} title="New group" size="lg">
        <CreateGroupForm onSubmit={handleCreate} onCancel={() => setCreating(false)} />
      </Modal>
    </div>
  );
}