import styles from "./MyBookings.module.css";

import {
  FaCheckCircle,
  FaClock,
  FaTimesCircle,
  FaSpinner,
} from "react-icons/fa";

export default function Bookings() {
  const bookings = [
    {
      id: "#DE-9942",
      name: "Luxury Sedan S-Class",
      date: "Oct 24 - Oct 28, 2023",
      price: "$540.00",
      status: "confirmed",
      image:
        "https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=1200&auto=format&fit=crop",
    },
    {
      id: "#DE-8821",
      name: "Electric Compact SUV",
      date: "Nov 02 - Nov 05, 2023",
      price: "$320.00",
      status: "pending",
      image:
        "https://images.unsplash.com/photo-1553440569-bcc63803a83d?q=80&w=1200&auto=format&fit=crop",
    },
    {
      id: "#DE-1104",
      name: "Convertible GT",
      date: "Sep 12 - Sep 15, 2023",
      price: "$450.00",
      status: "cancelled",
      image:
        "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?q=80&w=1200&auto=format&fit=crop",
    },
    {
      id: "#DE-4451",
      name: "Off-Road Explorer",
      date: "Oct 30 - Nov 04, 2023",
      price: "$780.00",
      status: "cancelling",
      image:
        "https://images.unsplash.com/photo-1544636331-e26879cd4d9b?q=80&w=1200&auto=format&fit=crop",
    },
  ];

  const renderStatus = (status) => {
    switch (status) {
      case "confirmed":
        return (
          <span className={`${styles.status} ${styles.confirmed}`}>
            <FaCheckCircle />
            CONFIRMED
          </span>
        );

      case "pending":
        return (
          <span className={`${styles.status} ${styles.pending}`}>
            <FaClock />
            PENDING
          </span>
        );

      case "cancelled":
        return (
          <span className={`${styles.status} ${styles.cancelled}`}>
            <FaTimesCircle />
            CANCELLED
          </span>
        );

      case "cancelling":
        return (
          <span className={`${styles.status} ${styles.cancelling}`}>
            <FaSpinner />
            CANCELLING
          </span>
        );

      default:
        return null;
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <div className={styles.container}>
        <div className={styles.heading}>
          <h1>My Bookings</h1>

          <p>
            Manage your current and upcoming vehicle rentals.
          </p>
        </div>

        <div className={styles.list}>
          {bookings.map((booking, index) => (
            <div className={styles.card} key={index}>
              <div className={styles.left}>
                <img
                  src={booking.image}
                  alt={booking.name}
                />

                <div className={styles.info}>

                  <p>{booking.name}</p>

                  <div className={styles.meta}>
                    <div>
                      <label>DATE RANGE</label>
                      <p>{booking.date}</p>
                    </div>

                    <div>
                      <label>TOTAL COST</label>
                      <p>{booking.price}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div className={styles.right}>
                {renderStatus(booking.status)}

                {booking.status === "confirmed" && (
                  <div className={styles.actions}>
                    <button className={styles.cancelBtn}>
                      Cancel Booking
                    </button>

                    <button className={styles.receiptBtn}>
                      View Receipt
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}