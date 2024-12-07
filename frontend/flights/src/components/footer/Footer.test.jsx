import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import Footer from './Footer';
import { BrowserRouter as Router } from 'react-router-dom'; // To wrap component with Router for Link to work

describe('Footer Component', () => {
  test('renders footer element', () => {
    render(
      <Router>
        <Footer />
      </Router>
    );
    const footerElement = screen.getByLabelText('Footer');
    expect(footerElement).toBeInTheDocument();
  });

  test('displays the correct copyright year', () => {
    const currentYear = new Date().getFullYear();
    render(
      <Router>
        <Footer />
      </Router>
    );
    const copyrightText = screen.getByText(`© ${currentYear} rTrip. All rights reserved.`);
    expect(copyrightText).toBeInTheDocument();
  });

  

});

describe('Footer Component', () => {
    const footerLinks = [
        { label: 'Terms of Service', path: '/terms' },
        { label: 'Privacy Policy', path: '/privacy' },
      ];
    test('renders footer links with correct paths', async () => {
      render(
        <Router>
          <Footer />
        </Router>
      );
  
      // Wait for each link to appear
      for (const link of footerLinks) {
        const linkElement = await screen.findByText(link.label);
        expect(linkElement).toBeInTheDocument();
        expect(linkElement).toHaveAttribute('href', link.path);
      }
    });
  });