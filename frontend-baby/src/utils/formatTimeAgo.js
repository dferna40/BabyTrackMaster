import dayjs from 'dayjs';

const formatTimeAgo = (dateInput) => {
  const date = dayjs(dateInput);
  const diffMinutes = dayjs().diff(date, 'minute');
  const hours = Math.floor(diffMinutes / 60);
  const minutes = diffMinutes % 60;
  const parts = [];
  if (hours > 0) parts.push(`${hours}h`);
  if (minutes > 0) parts.push(`${minutes}m`);
  if (parts.length === 0) parts.push('0m');
  return `Hace ${parts.join(' ')}`;
};

export default formatTimeAgo;

