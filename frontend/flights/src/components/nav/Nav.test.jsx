import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Nav from './Nav';

test('renders Nav component with correct title and links', () => {
  render(
    <MemoryRouter>
      <Nav />
    </MemoryRouter>
  );

  // Check if the title part "Trip" is rendered
  expect(screen.getByText(/Trip/i)).toBeInTheDocument();
  
  // Check if the Home link is rendered
  expect(screen.getByText(/Home/i)).toBeInTheDocument();
  
  // Check if the link has the correct path
  const homeLink = screen.getByText(/Home/i).closest('a');
  expect(homeLink).toHaveAttribute('href', '/');
});
