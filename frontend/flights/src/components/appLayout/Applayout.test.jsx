import { render, screen } from '@testing-library/react';
import AppLayout from './AppLayout';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';

// Mock the Nav and Footer components
vi.mock('../nav/Nav', () => ({
  __esModule: true,
  default: () => <div>Nav Component</div>,
}));

vi.mock('../footer/Footer', () => ({
  __esModule: true,
  default: () => <div>Footer Component</div>,
}));

test('renders Nav, Footer, and children correctly', () => {
  render(
    <MemoryRouter>
      <AppLayout>
        <div>Test Content</div>
      </AppLayout>
    </MemoryRouter>
  );

  screen.getByText(/Nav Component/i);


  screen.getByText(/Footer Component/i);

  screen.getByText(/Test Content/i);
});

test('renders empty children when no content is passed', () => {
  render(
    <MemoryRouter>
      <AppLayout />
    </MemoryRouter>
  );

  screen.queryByText(/Test Content/i);
});
