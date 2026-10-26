import { useState } from "react";
import { Controller, useForm, useWatch } from "react-hook-form";
import Input from "../ui/Input";
import Select from "../ui/Select";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import ParticipantSelector from "./ParticipantSelector";
import ShareInputs from "./ShareInputs";
import { createExpenseRequestForm } from "../../forms/InitialForms";
import { SPLIT_TYPES } from "../../data/splitTypes";
import { formatCurrencyCode, shortId } from "../../utils/format";
import { cn } from "../../utils/cn";

// Input-format check only (positive number, at most 2 decimals). The backend does the maths.
const MONEY_PATTERN = /^\d+(\.\d{1,2})?$/;
const isValidMoney = (v) => MONEY_PATTERN.test(String(v ?? "").trim()) && Number(v) > 0;

// onSubmit receives { title, amount, paidBy, splitType, participants, participantShare };
// the modal adds groupID and createdBy.
export default function AddExpenseForm({
  onSubmit,
  onCancel,
  members = [],
  currentUserId,
  currency,
  splitTypes = SPLIT_TYPES,
  allowPayerSelect = true,
  getDisplayName = (m) => `Member ${shortId(m.userId)}`,
  defaultValues,
  submitLabel = "Add expense",
  className,
}) {
  const [serverError, setServerError] = useState("");
  const {
    register,
    handleSubmit,
    control,
    formState: { errors, isSubmitting },
  } = useForm({
    defaultValues: {
      ...createExpenseRequestForm(),
      amount: "",
      paidBy: currentUserId,
      splitType: splitTypes[0]?.value ?? "",
      participants: members.map((m) => m.userId),
      shares: {}, // UI-only: { userId: "12.50" } for PERCENTAGE / AMOUNT
      ...defaultValues,
    },
  });

  const participants = useWatch({ control, name: "participants" }) ?? [];
  const splitType = useWatch({ control, name: "splitType" });
  const selectedMembers = members.filter((m) => participants.includes(m.userId));

  const submit = async (values) => {
    setServerError("");

    // only the selected participants' shares are sent (PERCENTAGE: percent values, e.g. 40)
    const participantShare =
      values.splitType === "EQUAL"
        ? {}
        : Object.fromEntries(values.participants.map((id) => [id, Number(values.shares[id])]));

    try {
      await onSubmit({
        title: values.title.trim(),
        amount: Number(values.amount),
        paidBy: allowPayerSelect ? values.paidBy : currentUserId,
        splitType: values.splitType,
        participants: values.participants,
        participantShare,
      });
    } catch (e) {
      setServerError(e.userMessage ?? e.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(submit)} noValidate className={cn("space-y-4", className)}>
      <Alert variant="error">{serverError}</Alert>

      <Input
        label="Title"
        placeholder="Dinner, groceries, rent..."
        error={errors.title?.message}
        {...register("title", {
          required: "Enter a title",
          maxLength: { value: 100, message: "Keep it under 100 characters" },
        })}
      />

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

      <div className="grid gap-4 sm:grid-cols-2">
        {allowPayerSelect && (
          <Select
            label="Paid by"
            options={members.map((m) => ({
              value: m.userId,
              label: m.userId === currentUserId ? "You" : getDisplayName(m),
            }))}
            error={errors.paidBy?.message}
            {...register("paidBy", { required: "Select who paid" })}
          />
        )}

        <Select
          label="Split type"
          options={splitTypes.map((t) => ({ value: t.value, label: t.label }))}
          {...register("splitType", { required: true })}
        />
      </div>

      <Controller
        name="participants"
        control={control}
        rules={{ validate: (v) => v.length > 0 || "Select at least one person" }}
        render={({ field, fieldState }) => (
          <ParticipantSelector
            members={members}
            value={field.value}
            onChange={field.onChange}
            currentUserId={currentUserId}
            getDisplayName={getDisplayName}
            error={fieldState.error?.message}
          />
        )}
      />

      {splitType !== "EQUAL" && (
        <Controller
          name="shares"
          control={control}
          rules={{
            validate: (shares, formValues) =>
              formValues.participants.every((id) => isValidMoney(shares?.[id])) ||
              "Enter a share greater than zero (up to 2 decimals) for everyone selected",
          }}
          render={({ field, fieldState }) => (
            <ShareInputs
              members={selectedMembers}
              value={field.value}
              onChange={field.onChange}
              mode={splitType}
              currency={currency}
              currentUserId={currentUserId}
              getDisplayName={getDisplayName}
              error={fieldState.error?.message}
            />
          )}
        />
      )}

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