import { useState } from "react";
import { useForm } from "react-hook-form";
import Input from "../ui/Input";
import Select from "../ui/Select";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import { createRegisterRequestForm } from "../../forms/authForms";
import { COUNTRIES } from "../../data/countries";
import { cn } from "../../utils/cn";

// onSubmit receives the clean payload (no confirmPassword, empty dob -> null).
export default function RegisterForm({
  onSubmit,
  defaultValues,
  countries = COUNTRIES,
  minPasswordLength = 8,
  submitLabel = "Create account",
  footer,
  className,
}) {
  const [serverError, setServerError] = useState("");
  const today = new Date().toISOString().split("T")[0];

  const {
    register,
    handleSubmit,
    getValues,
    formState: { errors, isSubmitting },
  } = useForm({
    defaultValues: { ...createRegisterRequestForm(), confirmPassword: "", ...defaultValues },
  });

  const submit = async (values) => {
    setServerError("");
    const payload = { ...values };
    delete payload.confirmPassword; // UI-only field, never sent
    try {
      await onSubmit(payload);
    } catch (e) {
      setServerError(e.userMessage ?? e.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(submit)} noValidate className={cn("space-y-4", className)}>
      <Alert variant="error">{serverError}</Alert>

      <Input
        label="Full name"
        autoComplete="name"
        error={errors.fullName?.message}
        {...register("fullName", {
          required: "Enter your full name",
          minLength: { value: 3, message: "Name must be at least 3 characters" },
          maxLength: { value: 100, message: "Name must be at most 100 characters" },
        })}
      />

      <Input
        label="Username"
        autoComplete="username"
        hint="Letters, numbers and underscores, 3-50 characters"
        error={errors.username?.message}
        {...register("username", {
          required: "Choose a username",
          pattern: { value: /^[a-zA-Z0-9_]{3,50}$/, message: "3-50 letters, numbers or underscores" },
        })}
      />

      <Input
        label="Email"
        type="email"
        autoComplete="email"
        error={errors.email?.message}
        {...register("email", {
          required: "Enter your email",
          minLength: { value: 5, message: "Email is too short" },
          maxLength: { value: 128, message: "Email must be at most 128 characters" },
          pattern: { value: /^\S+@\S+\.\S+$/, message: "Enter a valid email address" },
        })}
      />

      <div className="grid gap-4 sm:grid-cols-2">
        <Input
          label="Date of birth"
          type="date"
          max={today}
          autoComplete="bday"
          error={errors.dob?.message}
          {...register("dob", {
            required: "Enter your date of birth",
            validate: (v) => v <= today || "Date of birth cannot be in the future",
          })}
        />

        <Select
          label="Country"
          placeholder="Select country"
          options={countries.map((c) => ({ value: c.toLowerCase(), label: c }))}
          error={errors.country?.message}
          {...register("country", { required: "Select your country" })}
        />
      </div>

      <Input
        label="Password"
        type="password"
        autoComplete="new-password"
        error={errors.password?.message}
        {...register("password", {
          required: "Choose a password",
          minLength: { value: minPasswordLength, message: `At least ${minPasswordLength} characters` },
          maxLength: { value: 128, message: "At most 128 characters" },
        })}
      />

      <Input
        label="Confirm password"
        type="password"
        autoComplete="new-password"
        error={errors.confirmPassword?.message}
        {...register("confirmPassword", {
          required: "Re-enter your password",
          validate: (v) => v === getValues("password") || "Passwords do not match",
        })}
      />

      <Button type="submit" fullWidth loading={isSubmitting}>
        {submitLabel}
      </Button>

      {footer}
    </form>
  );
}