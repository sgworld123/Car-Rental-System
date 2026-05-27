import styles from "./VehicleDetails.module.css";

import {
  FaStar,
  FaBolt,
  FaTachometerAlt,
  FaSlidersH,
  FaChair,
  FaCheckCircle,
  FaCalendarAlt,
  FaUser,
  FaChevronDown,
  FaCog,
  FaHeart,
  FaShareAlt,
} from "react-icons/fa";

export default function VehicleDetails() {
  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <section className={styles.hero}>
        <img
          src="https://images.unsplash.com/photo-1617788138017-80ad40651399?q=80&w=1400&auto=format&fit=crop"
          alt="car"
        />

        <div className={styles.dots}>
          <span className={styles.activeDot}></span>
          <span></span>
          <span></span>
        </div>
      </section>

      <section className={styles.content}>
        <div className={styles.left}>
          <div className={styles.topInfo}>
            <div className={styles.badgeRow}>
              <span className={styles.badge}>
                Premium Sedan
              </span>

              <div className={styles.rating}>
                <FaStar />
                4.9 (124 reviews)
              </div>
            </div>

            <h1>Porsche Taycan 4S</h1>

            <p className={styles.description}>
              Experience the perfect blend of electric
              performance and high-end hospitality. The
              Taycan 4S offers a serene driving experience
              with cutting-edge technology, ensuring your
              journey is as smooth as it is sophisticated.
            </p>
          </div>

          <div className={styles.specGrid}>
            <div className={styles.specCard}>
              <FaBolt />

              <div>
                <label>Engine</label>
                <h4>Electric</h4>
              </div>
            </div>

            <div className={styles.specCard}>
              <FaTachometerAlt />

              <div>
                <label>Top Speed</label>
                <h4>250 km/h</h4>
              </div>
            </div>

            <div className={styles.specCard}>
              <FaSlidersH />

              <div>
                <label>Gearbox</label>
                <h4>Automatic</h4>
              </div>
            </div>

            <div className={styles.specCard}>
              <FaChair />

              <div>
                <label>Seats</label>
                <h4>4 Seats</h4>
              </div>
            </div>
          </div>

          <div className={styles.featuresSection}>
            <h2>Features & Amenities</h2>

            <div className={styles.featuresGrid}>
              <p>
                <FaCheckCircle />
                Adaptive Cruise Control
              </p>

              <p>
                <FaCheckCircle />
                Panoramic Sunroof
              </p>

              <p>
                <FaCheckCircle />
                Bose Surround Sound
              </p>

              <p>
                <FaCheckCircle />
                Heated & Ventilated Seats
              </p>
            </div>
          </div>

          <div className={styles.reviewSection}>
            <h2>Guest Reviews</h2>

            <div className={styles.reviewCard}>
              <div className={styles.reviewHeader}>
                <div className={styles.user}>
                  <img
                    src="https://i.pravatar.cc/100?img=12"
                    alt="user"
                  />

                  <div>
                    <h4>Alex Thompson</h4>
                    <span>October 2023</span>
                  </div>
                </div>

                <div className={styles.reviewStars}>
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                </div>
              </div>

              <p>
                "Incredible service from DriveEasy. The car
                was pristine, and the pickup process was the
                fastest I've ever experienced."
              </p>
            </div>
          </div>
        </div>

        <div className={styles.bookingCard}>
          <div className={styles.priceRow}>
            <div>
              <h2>$180</h2>
              <span>/ day</span>
            </div>

            <p>
              <FaCog />
              Inclusive of tax
            </p>
          </div>

          <div className={styles.field}>
            <label>Trip Dates</label>

            <div className={styles.input}>
              <FaCalendarAlt />
              <span>Oct 24 — Oct 27</span>
            </div>
          </div>

          <div className={styles.field}>
            <label>Driver Option</label>

            <div className={styles.input}>
              <div className={styles.driver}>
                <FaUser />
                <span>Self Drive</span>
              </div>

              <FaChevronDown />
            </div>
          </div>

          <div className={styles.bill}>
            <div>
              <span>$180 x 3 days</span>
              <span>$540</span>
            </div>

            <div>
              <span>Service fee</span>
              <span>$25</span>
            </div>
          </div>

          <div className={styles.total}>
            <span>Total</span>
            <h2>$565</h2>
          </div>

          <button className={styles.bookBtn}>
            Book Now
          </button>

          <p className={styles.note}>
            You won't be charged yet
          </p>

          <div className={styles.actions}>
            <button>
              <FaHeart />
              Save to Wishlist
            </button>

            <button>
              <FaShareAlt />
              Share Vehicle
            </button>
          </div>
        </div>
      </section>
    </div>
  );
}