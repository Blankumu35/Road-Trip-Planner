import React from 'react';
import { Link } from 'react-router-dom';
import styles from './Nav.module.css';

const navLinks = [
  { label: 'Home', path: '/' },
];

const Nav = () => {
  return (
    <header className={styles.header}>
      <div className={styles.title}>
        <i>r</i>Trip
      </div>
      <nav className={styles.nav} aria-label="Main Navigation">
        <ul className={styles.navList}>
          {navLinks.map((link) => (
            <li key={link.label} className={styles.navItem}>
              <Link to={link.path} className={styles.navLink}>
                {link.label}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </header>
  );
};

export default Nav;
