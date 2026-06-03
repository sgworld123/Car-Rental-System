
import styles from "./Profile.module.css";

import {
  FaStar,
  FaPhoneAlt,
  FaEnvelope,
  FaCarSide,
  FaUserEdit,
} from "react-icons/fa";

export default function ProfilePage() {

  const user = {
    name: "Shreyansh Gupta",
    email: "shreyansh@gmail.com",
    phone: "+91 9876543210",
    image: "https://i.pravatar.cc/300",
    rating: 4.9,
    bookings: 28,
  };

  return (
    <div className={styles.page}>

      <div className={styles.pattern}></div>

      <section className={styles.profileWrapper}>

        {/* LEFT PROFILE CARD */}

        <div className={styles.profileCard}>

          <div className={styles.imageWrap}>
            <img src={user.image} alt={user.name} />

            <div className={styles.online}></div>
          </div>

          <h1>{user.name}</h1>

          <p className={styles.memberTag}>
            Premium Member
          </p>

          {/* <button className={styles.editBtn}>
            <FaUserEdit />
            Edit Profile
          </button> */}

        </div>

        {/* RIGHT DETAILS */}

        <div className={styles.detailsSection}>

          {/* STATS */}

          <div className={styles.statsGrid}>

            <div className={styles.statCard}>
              <FaStar />

              <div>
                <label>USER RATING</label>
                <h2>{user.rating}</h2>
              </div>
            </div>

            <div className={styles.statCard}>
              <FaCarSide />

              <div>
                <label>TOTAL BOOKINGS</label>
                <h2>{user.bookings}</h2>
              </div>
            </div>

          </div>

          {/* INFO CARD */}

          <div className={styles.infoCard}>

            <h2>Personal Information</h2>

            <div className={styles.infoGrid}>

              <div className={styles.infoRow}>
                <span>
                  <FaEnvelope />
                </span>

                <div>
                  <label>Email Address</label>
                  <p>{user.email}</p>
                </div>
              </div>

              <div className={styles.infoRow}>
                <span>
                  <FaPhoneAlt />
                </span>

                <div>
                  <label>Phone Number</label>
                  <p>{user.phone}</p>
                </div>
              </div>

            </div>

          </div>

        </div>

      </section>

    </div>
  );
}

