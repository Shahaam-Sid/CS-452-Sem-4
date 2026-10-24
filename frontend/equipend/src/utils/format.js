export const shortId = (id) => (id ? String(id).slice(0, 8) : "-");

export const formatDate = (value) => {
  if (!value) return "";
  const d = new Date(value);
  return Number.isNaN(d.getTime())
    ? ""
    : d.toLocaleDateString(undefined, { year: "numeric", month: "short", day: "numeric" });
};

// The backend stores currency codes in lowercase; show them uppercase.
export const formatCurrencyCode = (code) => (code ? String(code).toUpperCase() : "");

// "EQUAL" -> "Equal"
export const humanize = (value) =>
  value ? value.charAt(0).toUpperCase() + value.slice(1).toLowerCase() : "";

export function formatMoney(amount, currency) {
  const n = Number(amount);
  if (Number.isNaN(n)) return "";
  const code = formatCurrencyCode(currency);
  if (!code) return n.toFixed(2);
  try {
    return new Intl.NumberFormat(undefined, { style: "currency", currency: code }).format(n);
  } catch {
    return `${n.toFixed(2)} ${code}`; // unknown currency code
  }
}