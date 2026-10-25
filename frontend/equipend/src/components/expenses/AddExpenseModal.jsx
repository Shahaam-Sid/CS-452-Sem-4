import Modal from "../ui/Modal";
import AddExpenseForm from "../forms/AddExpenseForm";
import useAuth from "../../hooks/useAuth";
import useToast from "../../hooks/useToast";
import { useCreateExpense } from "../../hooks/useExpenses";

// group: { id, currency }; members: [{ userId }]. Extra props go to AddExpenseForm.
export default function AddExpenseModal({ open, onClose, group, members, ...formProps }) {
  const { user } = useAuth();
  const toast = useToast();
  const createExpense = useCreateExpense();

  const handleSubmit = async (values) => {
    await createExpense.mutateAsync({ ...values, groupID: group.id, createdBy: user.id });
    toast.success("Expense added");
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title="Add expense" size="lg">
      <AddExpenseForm
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