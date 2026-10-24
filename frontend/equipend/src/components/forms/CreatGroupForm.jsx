import { useState } from "react";
import { useForm } from "react-hook-form";
import Input from "../ui/Input";
import Textarea from "../ui/Textarea";
import Select from "../ui/Select";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import { createGroupRequestForm } from "../../forms/InitialForms";
import { CURRENCIES } from "../../data/currencies";
import { cn } from "../../utils/cn";

// onSubmit receives { name, description, currency }; the page adds createdBy.
export default function CreateGroupForm({
  onSubmit,
  onCancel,
  defaultValues,
  currencies = CURRENCIES,
  submitLabel = "Create group",
  className,
}) {
  const [serverError, setServerError] = useState("");
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm({
    defaultValues: { ...createGroupRequestForm(), ...defaultValues },
  });

  const submit = async (values) => {
    setServerError("");
    try {
      await onSubmit({
        name: values.name.trim(),
        description: (values.description ?? "").trim(),
        currency: values.currency,
      });
    } catch (e) {
      setServerError(e.userMessage ?? e.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(submit)} noValidate className={cn("space-y-4", className)}>
      <Alert variant="error">{serverError}</Alert>

      <Input
        label="Group name"
        error={errors.name?.message}
        {...register("name", {
          required: "Enter a group name",
          minLength: { value: 2, message: "Name is too short" },
          maxLength: { value: 50, message: "Keep it under 50 characters" },
        })}
      />

      <Textarea
        label="Description (optional)"
        error={errors.description?.message}
        {...register("description", { maxLength: { value: 200, message: "Keep it under 200 characters" } })}
      />

      <Select
        label="Currency"
        placeholder="Select currency"
        options={currencies.map((c) => ({ value: c.code.toLowerCase(), label: `${c.code} - ${c.name}` }))}
        hint="You will be the first member. Add others from the group page."
        error={errors.currency?.message}
        {...register("currency", { required: "Select a currency" })}
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