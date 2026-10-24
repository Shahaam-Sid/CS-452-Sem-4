import { forwardRef, useId } from "react";
import { cn } from "../../utils/cn";

const Checkbox = forwardRef(function Checkbox(
  { label, hint, id, className, wrapperClassName, ...rest },
  ref
) {
  const autoId = useId();
  const inputId = id || autoId;

  return (
    <div className={cn("flex items-start gap-2", wrapperClassName)}>
      <input
        ref={ref}
        id={inputId}
        type="checkbox"
        className={cn("mt-0.5 h-4 w-4 rounded border-gray-300 text-brand-600 focus:ring-brand-500", className)}
        {...rest}
      />
      {(label || hint) && (
        <label htmlFor={inputId} className="text-sm text-gray-700">
          {label}
          {hint && <span className="block text-xs text-gray-500">{hint}</span>}
        </label>
      )}
    </div>
  );
});

export default Checkbox;