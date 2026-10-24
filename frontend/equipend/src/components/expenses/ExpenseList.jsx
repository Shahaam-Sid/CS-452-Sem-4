import ExpenseItem from "./ExpenseItem";
import { cn } from "../../utils/cn";

// getItemProps(expense) returns extra props for each ExpenseItem (currency, share, to...).
export default function ExpenseList({
  expenses = [],
  getItemProps = () => ({}),
  emptyText = "No expenses yet.",
  className,
}) {
  if (expenses.length === 0) return <p className="text-sm text-gray-500">{emptyText}</p>;

  return (
    <ul className={cn("divide-y divide-gray-100", className)}>
      {expenses.map((e) => (
        <li key={e.id}>
          <ExpenseItem expense={e} {...getItemProps(e)} />
        </li>
      ))}
    </ul>
  );
}