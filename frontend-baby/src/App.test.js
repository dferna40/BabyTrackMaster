import { render, screen } from '@testing-library/react';
import { GoogleOAuthProvider } from '@react-oauth/google';
import SignInSide from './sign-in-side/SignInSide';

test('renders sign in page', () => {
  render(
    <GoogleOAuthProvider clientId="test">
      <SignInSide />
    </GoogleOAuthProvider>
  );
  const heading = screen.getByText(/Acceder/i);
  expect(heading).toBeInTheDocument();
});
