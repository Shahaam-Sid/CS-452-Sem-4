import { useState } from "react";
import { useForm } from "react-hook-form";
import Input from "../ui/Input";
import Select from "../ui/Select";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import { createSettlementRequestForm } from "../../forms/InitialForms";
import { formatCurrencyCode, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

// Input-format check only (positive number, at most 2 decimals). The backend validates the rest.
const MONEY_PATTERN = /^\d+(\.\d{1,2})?$/;

// onSubmit receives { note, amount, paidTo, paidBy }; the modal adds the group id.
export default function SettleUpForm({
  onSubmit,
  onCancel,
  members = [],
  currentUserId,
  currency,
  allowPayerSelect = true,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  defaultValues,
  submitLabel = "Record payment",
  className,
}) {
  const [serverError, setServerError] = useState("");
  const {
    register,
    handleSubmit,
    getValues,
    formState: { errors, isSubmitting },
  } = useForm({
    defaultValues: {
      ...createSettlementRequestForm(),
      note: "",
      amount: "",
      paidBy: currentUserId,
      paidTo: "",
      ...defaultValues,
    },
  });

  const labelOf = (m) => (m.userId === currentUserId ? "You" : getDisplayName(m));

  const submit = async (values) => {
    setServerError("");
    try {
      await onSubmit({
        note: values.note.trim(),
        amount: Number(values.amount),
        paidTo: values.paidTo,
        paidBy: allowPayerSelect ? values.paidBy : currentUserId,
      });
    } catch (e) {
      setServerError(e.userMessage ?? e.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(submit)} noValidate className={cn("space-y-4", className)}>
      <Alert variant="error">{serverError}</Alert>

      <div className="grid gap-4 sm:grid-cols-2">
        {allowPayerSelect && (
          <Select
            label="Paid by"
            options={members.map((m) => ({ value: m.userId, label: labelOf(m) }))}
            error={errors.paidBy?.message}
            {...register("paidBy", { required: "Select who paid" })}
          />
        )}

        <Select
          label="Paid to"
          placeholder="Select person"
          options={members.map((m) => ({ value: m.userId, label: labelOf(m) }))}
          error={errors.paidTo?.message}
          {...register("paidTo", {
            required: "Select who was paid",
            validate: (v) =>
              v !== (allowPayerSelect ? getValues("paidBy") : currentUserId) ||
              "You cannot pay yourself",
          })}
        />
      </div>

      <Input
        label="Amount"
        inputMode="decimal"
        placeholder="0.00"
        hint={currency ? `In ${formatCurrencyCode(currency)}` : undefined}
        error={errors.amount?.message}
        {...register("amount", {
          required: "Enter an amount",
          pattern: { value: MONEY_PATTERN, message: "Use a number with up to 2 decimals" },
          validate: (v) => Number(v) > 0 || "Amount must be greater than zero",
        })}
      />

      <Input
        label="Note"
        placeholder="Cash, bank transfer, ..."
        error={errors.note?.message}
        {...register("note", {
          required: "Add a short note",
          maxLength: { value: 255, message: "Keep it under 255 characters" },
          validate: (v) => v.trim().length > 0 || "Add a short note",
        })}
      />

      <div className="flex justify-end gap-2 pt-2">
        {onCancel && (
          <Button variant="outline" onClick={onCancel}>
            Cancel
          </Button>
        )}
        <Button type="submit" loading={isSubmitting}>
          {submitLabel}
        </Button>
      </div>
    </form>
  );
}