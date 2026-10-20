import { forwardRef, useId } from "react";
import { cn } from "../../utils/cn";

const sizes = { sm: "h-8 text-sm", md: "h-10 text-sm", lg: "h-12 text-base" };

const Select = forwardRef(function Select(
  { label, error, hint, options = [], placeholder, size = "md", id, className, wrapperClassName, children, ...rest },
  ref
) {
  const autoId = useId();
  const selectId = id || autoId;

  return (
    <div className={cn("w-full", wrapperClassName)}>
      {label && (
        <label htmlFor={selectId} className="mb-1 block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <select
        ref={ref}
        id={selectId}
        aria-invalid={!!error}
        className={cn(
          "w-full rounded-lg border bg-white px-3 text-gray-900",
          "focus:outline-none focus:ring-2 disabled:bg-gray-100",
          sizes[size],
          error
            ? "border-red-400 focus:ring-red-200"
            : "border-gray-300 focus:border-brand-500 focus:ring-brand-200",
          className
        )}
        {...rest}
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
        {children}
      </select>
      {error ? (
        <p className="mt-1 text-xs text-red-600">{error}</p>
      ) : (
        hint && <p className="mt-1 text-xs text-gray-500">{hint}</p>
      )}
    </div>
  );
});

export default Select;