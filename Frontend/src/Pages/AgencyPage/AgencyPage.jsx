import styles from "./AgencyPage.module.css";

import {
  FaArrowLeft,
  FaSun,
  FaMapMarkerAlt,
  FaStar,
  FaPhoneAlt,
  FaEnvelope,
  FaMapMarkedAlt,
  FaShieldAlt,
  FaSlidersH,
  FaCog,
  FaBolt,
  FaChair,
  FaCar,
} from "react-icons/fa";

export default function AgencyPage() {
  const vehicles = [
    {
      name: "BMW 7 Series",
      image:
        "https://images.unsplash.com/photo-1555215695-3004980ad54e?q=80&w=1200&auto=format&fit=crop",
      tag: "Luxury",
      features: ["Automatic", "Hybrid"],
      price: "$1.25",
      icon1: <FaCog />,
      icon2: <FaBolt />,
    },
    {
      name: "Tesla Model S",
      image:
        "https://images.unsplash.com/photo-1560958089-b8a1929cea89?q=80&w=1200&auto=format&fit=crop",
      tag: "Electric",
      features: ["Electric", "5 Seats"],
      price: "$1.40",
      icon1: <FaBolt />,
      icon2: <FaChair />,
    },
    {
      name: "Audi Q8",
      image:
        "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?q=80&w=1200&auto=format&fit=crop",
      tag: "SUV",
      features: ["AWD", "Extra Space"],
      price: "$1.15",
      icon1: <FaCar />,
      icon2: <FaChair />,
    },
  ];

  const reviews = [
    {
      name: "James Harrington",
      date: "SEPTEMBER 12, 2023",
      text:
        "Impeccable service. The car was spotless and ready for me as soon as I arrived.",
      avatar: "https://i.pravatar.cc/100?img=11",
    },
    {
      name: "Sophia Chen",
      date: "AUGUST 28, 2023",
      text:
        "Booked a Tesla for the weekend. Staff was very helpful and the process was seamless.",
      avatar: "https://i.pravatar.cc/100?img=32",
    },
    {
      name: "Marcello Rossi",
      date: "AUGUST 15, 2023",
      text:
        "Elite Grand always delivers. Their luxury vehicles are maintained perfectly.",
      avatar: "https://i.pravatar.cc/100?img=14",
    },
  ];

  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <header className={styles.topbar}>
        <div className={styles.brand}>
          <FaArrowLeft />
          <span>DriveEasy</span>
        </div>

        <button className={styles.themeBtn}>
          <FaSun />
        </button>
      </header>

      <section className={styles.hero}>
        <img
          src="https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=1400&auto=format&fit=crop"
          alt="agency"
        />

        <div className={styles.overlay}></div>

        <div className={styles.heroContent}>
          <div>
            <h1>Elite Grand Car Rentals</h1>

            <p>
              <FaMapMarkerAlt />
              Mayfair District, London
            </p>
          </div>

          <div className={styles.rating}>
            <FaStar />
            4.9 (1.2k Reviews)
          </div>
        </div>
      </section>

      <section className={styles.infoSection}>
        <div className={styles.aboutCard}>
          <h3>About the Agency</h3>

          <p>
            Providing premium automotive experiences since 2012.
            Elite Grand specializes in luxury sedans, executive SUVs,
            and high-performance sports cars for discerning travelers.
          </p>

          <div className={styles.contactGrid}>
            <div>
              <span>
                <FaPhoneAlt />
              </span>

              <div>
                <label>PHONE</label>
                <p>+44 20 7946 0123</p>
              </div>
            </div>

            <div>
              <span>
                <FaEnvelope />
              </span>

              <div>
                <label>EMAIL</label>
                <p>concierge@elitegrand.com</p>
              </div>
            </div>

            <div className={styles.addressRow}>
              <span>
                <FaMapMarkedAlt />
              </span>

              <div>
                <label>ADDRESS</label>
                <p>
                  12B Berkeley Square, Mayfair, London W1J 6EB,
                  United Kingdom
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className={styles.sideCards}>
          <div className={styles.hoursCard}>
            <h3>Operational Hours</h3>

            <div className={styles.hours}>
              <div>
                <span>Monday - Friday</span>
                <span>08:00 - 22:00</span>
              </div>

              <div>
                <span>Saturday</span>
                <span>09:00 - 20:00</span>
              </div>

              <div>
                <span>Sunday</span>
                <span>10:00 - 18:00</span>
              </div>

              <div className={styles.closed}>
                <span>Public Holidays</span>
                <span>Closed</span>
              </div>
            </div>
          </div>

          <div className={styles.verifyCard}>
            <div>
              <h4>Verified Agency</h4>
              <p>Since 2012</p>
            </div>

            <FaShieldAlt />
          </div>
        </div>
      </section>

      <section className={styles.vehiclesSection}>
        <div className={styles.sectionHeader}>
          <h2>Available Vehicles</h2>

          <button className={styles.filterBtn}>
            <FaSlidersH />
          </button>
        </div>

        <div className={styles.vehicleGrid}>
          {vehicles.map((vehicle, index) => (
            <div className={styles.vehicleCard} key={index}>
              <img src={vehicle.image} alt={vehicle.name} />

              <div className={styles.vehicleContent}>
                <div className={styles.vehicleTop}>
                  <h3>{vehicle.name}</h3>

                  <span>{vehicle.tag}</span>
                </div>

                <div className={styles.features}>
                  <p>
                    {vehicle.icon1}
                    {vehicle.features[0]}
                  </p>

                  <p>
                    {vehicle.icon2}
                    {vehicle.features[1]}
                  </p>
                </div>

                <div className={styles.vehicleBottom}>
                  <div>
                    <label>Price per km</label>
                    <h4>{vehicle.price}</h4>
                  </div>

                  <button>Book Now</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      <section className={styles.reviewSection}>
        <h2>Customer Reviews</h2>

        <div className={styles.reviewGrid}>
          {reviews.map((review, index) => (
            <div className={styles.reviewCard} key={index}>
              <div className={styles.reviewHeader}>
                <img src={review.avatar} alt={review.name} />

                <div>
                  <h4>{review.name}</h4>
                  <div className={styles.stars}>
                    <FaStar />
                    <FaStar />
                    <FaStar />
                    <FaStar />
                    <FaStar />
                  </div>
                </div>
              </div>

              <p className={styles.reviewText}>
                "{review.text}"
              </p>

              <span className={styles.reviewDate}>
                {review.date}
              </span>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}