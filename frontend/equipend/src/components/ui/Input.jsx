import { forwardRef, useId } from "react";
import { cn } from "../../utils/cn";

const sizes = { sm: "h-8 text-sm", md: "h-10 text-sm", lg: "h-12 text-base" };

const Input = forwardRef(function Input(
  { label, error, hint, leftIcon, size = "md", id, className, wrapperClassName, ...rest },
  ref
) {
  const autoId = useId();
  const inputId = id || autoId;

  return (
    <div className={cn("w-full", wrapperClassName)}>
      {label && (
        <label htmlFor={inputId} className="mb-1 block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <div className="relative">
        {leftIcon && (
          <span className="pointer-events-none absolute inset-y-0 left-3 flex items-center text-gray-400">
            {leftIcon}
          </span>
        )}
        <input
          ref={ref}
          id={inputId}
          aria-invalid={!!error}
          className={cn(
            "w-full rounded-lg border bg-white px-3 text-gray-900 placeholder-gray-400",
            "focus:outline-none focus:ring-2 disabled:bg-gray-100",
            sizes[size],
            leftIcon && "pl-10",
            error
              ? "border-red-400 focus:ring-red-200"
              : "border-gray-300 focus:border-brand-500 focus:ring-brand-200",
            className
          )}
          {...rest}
        />
      </div>
      {error ? (
        <p className="mt-1 text-xs text-red-600">{error}</p>
      ) : (
        hint && <p className="mt-1 text-xs text-gray-500">{hint}</p>
      )}
    </div>
  );
});

export default Input;