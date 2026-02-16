import React from 'react';
import { NavLink } from 'react-router-dom';

const Header = () => {
  const styles = {
    header: {
      width: '100%',
      display: 'flex',
      alignItems: 'center',
      padding: '0 40px',
      height: '70px',
      backgroundColor: '#1e1e1e',
      borderBottom: '1px solid #333',
      color: '#fff',
      boxSizing: 'border-box',
      position: 'sticky',
      top: 0,
      zIndex: 1000,
    },

    left: {
      flex: 1,
      display: 'flex',
      alignItems: 'center',
    },

    center: {
      flex: 1,
      display: 'flex',
      justifyContent: 'center',
      gap: '20px',
      minWidth: 0, // 🔥 prevents overflow
    },

    right: {
      flex: 1,
      display: 'flex',
      justifyContent: 'flex-end',
      alignItems: 'center',
    },

    logo: {
      fontSize: '24px',
      fontWeight: 'bold',
      color: '#6200ea',
      cursor: 'pointer',
      letterSpacing: '1px',
    },

    navButton: {
      background: 'transparent',
      border: 'none',
      color: '#e0e0e0',
      fontSize: '16px',
      cursor: 'pointer',
      padding: '8px 12px',
      borderRadius: '4px',
      transition: 'background-color 0.2s, color 0.2s',
      whiteSpace: 'nowrap',
    },

    themeButton: {
      padding: '8px 16px',
      backgroundColor: '#2c2c2c',
      border: '1px solid #444',
      borderRadius: '20px',
      color: '#e0e0e0',
      cursor: 'pointer',
      fontSize: '14px',
      display: 'flex',
      alignItems: 'center',
      gap: '8px',
      whiteSpace: 'nowrap',
    },
  };

  const handleMouseEnter = (e) => {
    e.target.style.backgroundColor = '#333';
    e.target.style.color = '#fff';
  };

  const handleMouseLeave = (e) => {
    e.target.style.backgroundColor = 'transparent';
    e.target.style.color = '#e0e0e0';
  };

  return (
    <header style={styles.header}>
      <div style={styles.left}>
        <div style={styles.logo}>MY LOGO</div>
      </div>

      <nav style={styles.center}>
        <NavLink to={"/dashboard"} style={styles.navButton}  onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
          Home
        </NavLink>
        <NavLink to={"/dashboard/bookings"} style={styles.navButton} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
          Bookings
        </NavLink>
        <NavLink to={"/profile"} style={styles.navButton} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
          Profile
        </NavLink>
        <NavLink to={"/settings"} style={styles.navButton} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
          Settings
        </NavLink>
      </nav>

      <div style={styles.right}>
        <button style={styles.themeButton}>
          <span>☀️ / 🌙</span>
          <span>Switch Theme</span>
        </button>
      </div>
    </header>
  );
};

export default Header;
