import React from 'react';

const Footer = () => {
  const styles = {
    footer: {
      backgroundColor: '#1e1e1e',
      color: '#b0b0b0',
      padding: '50px 40px 20px 40px',
      borderTop: '1px solid #333',
      fontFamily: 'Arial, sans-serif',
    },
    container: {
      display: 'flex',
      justifyContent: 'space-between',
      flexWrap: 'wrap',
      gap: '40px',
      maxWidth: '1200px',
      margin: '0 auto',
    },
    section: {
      flex: '1',
      minWidth: '200px',
    },
    heading: {
      color: '#ffffff',
      fontSize: '18px',
      fontWeight: '600',
      marginBottom: '20px',
    },
    text: {
      fontSize: '14px',
      lineHeight: '1.6',
    },
    list: {
      listStyle: 'none',
      padding: 0,
      margin: 0,
    },
    listItem: {
      marginBottom: '10px',
    },
    link: {
      color: '#b0b0b0',
      textDecoration: 'none',
      fontSize: '14px',
      cursor: 'pointer',
      transition: 'color 0.2s',
    },
    bottomBar: {
      marginTop: '40px',
      paddingTop: '20px',
      borderTop: '1px solid #333',
      textAlign: 'center',
      fontSize: '12px',
      color: '#666',
    }
  };

  // Hover effect helper
  const handleLinkHover = (e, isHover) => {
    e.target.style.color = isHover ? '#6200ea' : '#b0b0b0';
  };

  return (
    <footer style={styles.footer}>
      <div style={styles.container}>
        
        {/* Section 1: About */}
        <div style={styles.section}>
          <h4 style={styles.heading}>About Us</h4>
          <p style={styles.text}>
            We are dedicated to providing the best user experience with modern design and robust functionality. 
            Building the future, one component at a time.
          </p>
        </div>

        {/* Section 2: Quick Links */}
        <div style={styles.section}>
          <h4 style={styles.heading}>Quick Links</h4>
          <ul style={styles.list}>
            {['Home', 'Services', 'Portfolio', 'Contact', 'FAQ'].map((item) => (
              <li key={item} style={styles.listItem}>
                <a 
                  href={`#${item.toLowerCase()}`} 
                  style={styles.link}
                  onMouseEnter={(e) => handleLinkHover(e, true)}
                  onMouseLeave={(e) => handleLinkHover(e, false)}
                >
                  {item}
                </a>
              </li>
            ))}
          </ul>
        </div>

        {/* Section 3: Contact Info */}
        <div style={styles.section}>
          <h4 style={styles.heading}>Contact</h4>
          <p style={styles.text}>
            Email: info@example.com<br />
            Phone: +1 (234) 567-890<br />
            Address: 123 Tech Lane, Silicon Valley, CA
          </p>
        </div>

        {/* Section 4: Social Media */}
        <div style={styles.section}>
          <h4 style={styles.heading}>Follow Us</h4>
          <div style={{ display: 'flex', gap: '15px' }}>
            {['Twitter', 'GitHub', 'LinkedIn', 'Discord'].map((social) => (
              <a 
                key={social}
                href="#" 
                style={styles.link}
                onMouseEnter={(e) => handleLinkHover(e, true)}
                onMouseLeave={(e) => handleLinkHover(e, false)}
              >
                {social}
              </a>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom Bar */}
      <div style={styles.bottomBar}>
        © {new Date().getFullYear()} YourBrand Name. All rights reserved.
      </div>
    </footer>
  );
};

export default Footer;