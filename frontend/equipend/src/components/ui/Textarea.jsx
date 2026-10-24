import { forwardRef, useId } from "react";
import { cn } from "../../utils/cn";

const Textarea = forwardRef(function Textarea(
  { label, error, hint, rows = 3, id, className, wrapperClassName, ...rest },
  ref
) {
  const autoId = useId();
  const textareaId = id || autoId;

  return (
    <div className={cn("w-full", wrapperClassName)}>
      {label && (
        <label htmlFor={textareaId} className="mb-1 block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <textarea
        ref={ref}
        id={textareaId}
        rows={rows}
        aria-invalid={!!error}
        className={cn(
          "w-full resize-y rounded-lg border bg-white px-3 py-2 text-sm text-gray-900 placeholder-gray-400",
          "focus:outline-none focus:ring-2 disabled:bg-gray-100",
          error
            ? "border-red-400 focus:ring-red-200"
            : "border-gray-300 focus:border-brand-500 focus:ring-brand-200",
          className
        )}
        {...rest}
      />
      {error ? (
        <p className="mt-1 text-xs text-red-600">{error}</p>
      ) : (
        hint && <p className="mt-1 text-xs text-gray-500">{hint}</p>
      )}
    </div>
  );
});

export default Textarea;