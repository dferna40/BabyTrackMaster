export const parseDurationToHours = (str) => {
  if (str == null) return 0;
  if (typeof str === 'number') return str;
  const match = String(str)
    .trim()
    .match(/^(?:\s*(\d+)h)?\s*(?:([0-9]+)m)?$/i);
  if (!match) return 0;
  const hours = parseInt(match[1] || '0', 10);
  const minutes = parseInt(match[2] || '0', 10);
  return hours + minutes / 60;
};

export default parseDurationToHours;
