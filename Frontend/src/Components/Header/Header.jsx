import styles from "./Header.module.css";
import { FaCarSide, FaSun } from "react-icons/fa";
import { NavLink } from "react-router-dom";

export default function Header() {
  return (
    <header className={styles.navbar}>
      <div className={styles.logo}>
        <FaCarSide />
        <span>DriveEasy</span>
      </div>

      <nav className={styles.navLinks}>
        <NavLink to="/dashboard" end>
          Home
        </NavLink>
        <NavLink to="/dashboard/bookings">Bookings</NavLink>
        <NavLink to="/dashboard/profile">Profile</NavLink>
        <NavLink to="/dashboard/settings">About</NavLink>
      </nav>

      <div className={styles.actions}>
        <button className={styles.iconBtn}>
          <FaSun />
        </button>

        <img
          src="https://i.pravatar.cc/100"
          alt="profile"
          className={styles.avatar}
        />
      </div>
    </header>
  );
}