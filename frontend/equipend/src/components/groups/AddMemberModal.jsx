import { useState } from "react";
import Modal from "../ui/Modal";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import MemberPicker from "../forms/MemberPicker";
import useToast from "../../hooks/useToast";
import { useAddMember } from "../../hooks/useGroups";

export default function AddMemberModal({ open, onClose, groupId, existingMemberIds = [] }) {
  const [selected, setSelected] = useState([]);
  const [error, setError] = useState("");
  const addMember = useAddMember(groupId);
  const toast = useToast();

  const close = () => {
    setSelected([]);
    setError("");
    onClose();
  };

  const submit = async () => {
    setError("");
    try {
      await addMember.mutateAsync(selected[0].id);
      toast.success("Member added");
      close();
    } catch (e) {
      setError(e.userMessage ?? e.message);
    }
  };

  return (
    <Modal
      open={open}
      onClose={close}
      title="Add member"
      footer={
        <>
          <Button variant="outline" onClick={close}>
            Cancel
          </Button>
          <Button onClick={submit} loading={addMember.isPending} disabled={selected.length === 0}>
            Add member
          </Button>
        </>
      }
    >
      <div className="space-y-4">
        <Alert variant="error">{error}</Alert>
        <MemberPicker
          multiple={false}
          label="Find a user"
          selected={selected}
          onChange={setSelected}
          isExcluded={(u) => existingMemberIds.includes(u.id)}
        />
      </div>
    </Modal>
  );
}