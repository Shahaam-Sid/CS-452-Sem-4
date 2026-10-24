import Modal from "../ui/Modal";
import SettleUpForm from "../forms/SettleUpForm";
import useAuth from "../../hooks/useAuth";
import { useCreateSettlement } from "../../hooks/useSettlements";

// group: { id, currency }; members: [{ userId }]. Extra props go to SettleUpForm.
export default function SettleUpModal({ open, onClose, group, members, ...formProps }) {
  const { user } = useAuth();
  const createSettlement = useCreateSettlement();

  const handleSubmit = async (values) => {
    await createSettlement.mutateAsync({ ...values, group: group.id });
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title="Record a payment" size="lg">
      <SettleUpForm
        onSubmit={handleSubmit}
        onCancel={onClose}
        members={members}
        currentUserId={user?.id}
        currency={group.currency}
        {...formProps}
      />
    </Modal>
  );
}