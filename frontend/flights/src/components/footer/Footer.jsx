import React from 'react';
import { Link } from 'react-router-dom';
import styles from './Footer.module.css';

const footerLinks = [
  { label: 'Terms of Service', path: '/terms' },
  { label: 'Privacy Policy', path: '/privacy' },
];

function Footer() {
  return (
    <footer className={styles.footer} aria-label="Footer">
      <div className={styles.footerContent}>
        <p className={styles.copy}>
          &copy; {new Date().getFullYear()} rTrip. All rights reserved.
        </p>
        <nav className={styles.footerNav} aria-label="Footer Navigation">
          <ul className={styles.navList}>
            {footerLinks.map((link) => (
              <li key={link.label} className={styles.navItem}>
                <Link to={link.path} className={styles.footerLink}>
                  {link.label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </div>
    </footer>
  );
}

export default Footer;
