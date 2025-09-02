import { render, screen } from '@testing-library/react';
import App from './App';

test('renders BabyTrackMaster text', () => {
  render(<App />);
  const textElement = screen.getByText(/BabyTrackMaster/i);
  expect(textElement).toBeInTheDocument();
});
