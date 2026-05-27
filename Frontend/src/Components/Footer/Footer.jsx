import styles from "./Footer.module.css";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <p>© 2024 DriveEasy Global Mobility. All rights reserved.</p>

      <div className={styles.links}>
        <span>Security</span>
        <span>Cookies</span>
        <span>Privacy</span>
      </div>
    </footer>
  );
}