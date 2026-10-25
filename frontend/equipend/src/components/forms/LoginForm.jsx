import { useState } from "react";
import { useForm } from "react-hook-form";
import { Lock, User } from "lucide-react";
import Input from "../ui/Input";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import { createAuthenticateRequestForm } from "../../forms/authForms";
import { cn } from "../../utils/cn";

export default function LoginForm({ onSubmit, defaultValues, submitLabel = "Log in", footer, className }) {
  const [serverError, setServerError] = useState("");
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm({ defaultValues: { ...createAuthenticateRequestForm(), ...defaultValues } });

  const submit = async (values) => {
    setServerError("");
    try {
      await onSubmit(values);
    } catch (e) {
      setServerError(e.userMessage ?? e.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(submit)} noValidate className={cn("space-y-4", className)}>
      <Alert variant="error">{serverError}</Alert>

      <Input
        label="Username or email"
        autoComplete="username"
        leftIcon={<User size={16} />}
        error={errors.identifier?.message}
        {...register("identifier", {
          required: "Enter your username or email",
          minLength: { value: 3, message: "At least 3 characters" },
          maxLength: { value: 128, message: "At most 128 characters" },
        })}
      />

      <Input
        label="Password"
        type="password"
        autoComplete="current-password"
        leftIcon={<Lock size={16} />}
        error={errors.password?.message}
        {...register("password", {
          required: "Enter your password",
          minLength: { value: 8, message: "At least 8 characters" },
          maxLength: { value: 128, message: "At most 128 characters" },
        })}
      />

      <Button type="submit" fullWidth loading={isSubmitting}>
        {submitLabel}
      </Button>

      {footer}
    </form>
  );
}