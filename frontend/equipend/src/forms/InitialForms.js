export const ExpenseRequestForm = Object.freeze({
  title: "",
  amount: null,
  splitType: null,
  participants: [],
  participantShare: {}
});

export const ExpenseSplitRequestForm = Object.freeze({
  user: null,
  expense: null,
  shareAmount: null
});

export const GroupMemberRequestForm = Object.freeze({
  user: null,
  group: null,
});

export const GroupRequestForm = Object.freeze({
  name: "",
  description: "",
  currency: "",
});

export const SettlementRequestForm = Object.freeze({
  note: "",
  amount: null,
  paidTo: null,
  group: null
});


export const createExpenseRequestForm = () => ({ ...ExpenseRequestForm, participants: [], participantShare: {} });
export const createExpenseSplitRequestForm = () => ({ ...ExpenseSplitRequestForm });
export const createGroupMemberRequestForm = () => ({ ...GroupMemberRequestForm });
export const createGroupRequestForm = () => ({ ...GroupRequestForm });
export const createSettlementRequestForm = () => ({ ...SettlementRequestForm });