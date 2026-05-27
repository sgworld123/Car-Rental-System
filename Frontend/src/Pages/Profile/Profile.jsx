import styles from "./Profile.module.css";

import {
  FaSun,
  FaPhoneAlt,
  FaPen,
} from "react-icons/fa";

export default function Profile() {
  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <div className={styles.glow}></div>

      <div className={styles.card}>
        <div className={styles.banner}></div>

        <div className={styles.avatar}>
          <span>J</span>
        </div>

        <div className={styles.content}>
          <h1>Julianne Deville</h1>

          <p className={styles.email}>
            julianne.deville@example.com
          </p>

          <div className={styles.contactRow}>
            <div className={styles.phone}>
              <FaPhoneAlt />
              <span>+1 (555) 924-1284</span>
            </div>

            <div className={styles.verified}>
              Verified
            </div>
          </div>

          <button className={styles.editBtn}>
            <FaPen />
            Edit Profile
          </button>
        </div>

        <div className={styles.stats}>
          <div>
            <h2>12</h2>
            <p>RENTALS</p>
          </div>

          <div>
            <h2>4.9</h2>
            <p>RATING</p>
          </div>
        </div>
      </div>
    </div>
  );
}