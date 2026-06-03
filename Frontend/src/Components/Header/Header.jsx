import styles from "./Header.module.css";
import { FaCarSide, FaSun } from "react-icons/fa";
import { NavLink } from "react-router-dom";

export default function Header() {
  return (
    <header className={styles.navbar}>

      {/* Logo */}
      <div className={styles.logo}>
        <FaCarSide />
        <span>DriveEasy</span>
      </div>

      {/* Navigation */}
      <nav className={styles.navLinks}>

        <NavLink
          to="/dashboard"
          end
          className={({ isActive }) =>
            isActive ? styles.active : ""
          }
        >
          Home
        </NavLink>

        <NavLink
          to="/dashboard/bookings"
          className={({ isActive }) =>
            isActive ? styles.active : ""
          }
        >
          Bookings
        </NavLink>

        <NavLink
          to="/dashboard/profile"
          className={({ isActive }) =>
            isActive ? styles.active : ""
          }
        >
          Profile
        </NavLink>

        <NavLink
          to="/dashboard/aboutme"
          className={({ isActive }) =>
            isActive ? styles.active : ""
          }
        >
          About Me
        </NavLink>

      </nav>

      {/* Actions */}
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