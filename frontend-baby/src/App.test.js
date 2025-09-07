import { render, screen } from '@testing-library/react';

test.skip('renders BabyTrackMaster text', async () => {
  const { default: App } = await import('./App');
  render(<App />);
  const textElement = screen.getByText(/BabyTrackMaster/i);
  expect(textElement).toBeInTheDocument();
});
