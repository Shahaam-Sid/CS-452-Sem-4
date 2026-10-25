import Modal from "../ui/Modal";
import SettleUpForm from "../forms/SettleUpForm";
import useAuth from "../../hooks/useAuth";
import useToast from "../../hooks/useToast";
import { useCreateSettlement } from "../../hooks/useSettlements";

// group: { id, currency }; members: [{ userId }]. Extra props go to SettleUpForm.
export default function SettleUpModal({ open, onClose, group, members, ...formProps }) {
  const { user } = useAuth();
  const toast = useToast();
  const createSettlement = useCreateSettlement();

  const handleSubmit = async (values) => {
    await createSettlement.mutateAsync({ ...values, group: group.id });
    toast.success("Payment recorded");
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