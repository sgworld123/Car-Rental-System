import styles from "./MyBookings.module.css";
import { FaCheckCircle, FaClock, FaTimesCircle, FaSpinner } from "react-icons/fa";
import { useEffect, useState, useRef } from "react";
import { getUserBookings } from "../../services/bookingService";
import { useCancelBooking } from "../../Hooks/useCancelBooking";

export default function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [cancellingIds, setCancellingIds] = useState(new Set());
  const pollRefs = useRef({});

  const { cancel, cancelError } = useCancelBooking();

  const fetchBookings = async () => {
    try {
      const response = await getUserBookings();
      const data = Array.isArray(response.data)
        ? response.data
        : response.data
        ? [response.data]
        : [];
      setBookings(data);
      return data;
    } catch (err) {
      console.error("Error fetching bookings:", err);
      setError("Failed to load bookings.");
      return [];
    }
  };

  useEffect(() => {
    fetchBookings().finally(() => setLoading(false));
    return () => {
      Object.values(pollRefs.current).forEach(({ interval, timeout }) => {
        clearInterval(interval);
        clearTimeout(timeout);
      });
    };
  }, []);

  const handleCancel = async (id) => {
    try {
      await cancel(id);

      setCancellingIds((prev) => new Set(prev).add(id));
      setBookings((prev) =>
        prev.map((b) => (b.bookingId === id ? { ...b, status: "CANCELLING" } : b))
      );

      const interval = setInterval(async () => {
        const data = await fetchBookings();
        const updated = data.find((b) => b.bookingId === id);
        if (updated?.status === "CANCELLED") {
          clearInterval(interval);
          clearTimeout(pollRefs.current[id]?.timeout);
          delete pollRefs.current[id];
          setCancellingIds((prev) => {
            const next = new Set(prev);
            next.delete(id);
            return next;
          });
        }
      }, 2000);

      const timeout = setTimeout(() => {
        clearInterval(interval);
        delete pollRefs.current[id];
        setCancellingIds((prev) => {
          const next = new Set(prev);
          next.delete(id);
          return next;
        });
      }, 30000);

      pollRefs.current[id] = { interval, timeout };
    } catch (e) {
      console.error("Cancel failed", e);
      setCancellingIds((prev) => {
        const next = new Set(prev);
        next.delete(id);
        return next;
      });
    }
  };

  const formatDateRange = (from, to) => {
    const options = { month: "short", day: "numeric", year: "numeric" };
    const f = new Date(from).toLocaleDateString("en-US", options);
    const t = new Date(to).toLocaleDateString("en-US", options);
    return `${f} - ${t}`;
  };

  const formatCost = (cost) => `₹${Number(cost).toLocaleString("en-IN")}`;
  const shortId = (id) => `#${id?.slice(-8).toUpperCase()}`;

  const renderStatus = (status) => {
  switch (status?.toUpperCase()) {
    case "CONFIRMED":
      return (
        <span className={`${styles.status} ${styles.confirmed}`}>
          <FaCheckCircle />
          CONFIRMED
        </span>
      );

    case "PENDING":
      return (
        <span className={`${styles.status} ${styles.pending}`}>
          <FaClock />
          PENDING
        </span>
      );

    case "CANCELLED":
      return (
        <span className={`${styles.status} ${styles.cancelled}`}>
          <FaTimesCircle />
          CANCELLED
        </span>
      );

    case "REFUNDED":
      return (
        <span className={`${styles.status} ${styles.cancelled}`}>
          <FaTimesCircle />
          REFUNDED
        </span>
      );

    case "CANCELLING":
      return (
        <span className={`${styles.status} ${styles.cancelling}`}>
          <FaSpinner className={styles.spin} />
          CANCELLING
        </span>
      );

    default:
      return (
        <span className={`${styles.status} ${styles.pending}`}>
          {status}
        </span>
      );
  }
};

  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <div className={styles.container}>
        <div className={styles.heading}>
          <h1>My Bookings</h1>
          <p>Manage your current and upcoming vehicle rentals.</p>
        </div>

        {loading && <p className={styles.infoText}>Loading bookings...</p>}
        {error && <p className={styles.errorText}>{error}</p>}
        {cancelError && <p className={styles.errorText}>Cancel failed. Please try again.</p>}
        {!loading && bookings.length === 0 && (
          <p className={styles.infoText}>No bookings yet.</p>
        )}

        <div className={styles.list}>
          {[...bookings].reverse().map((booking) => {
            const isCancelling = cancellingIds.has(booking.bookingId);

            return (
              <div className={styles.card} key={booking.bookingId}>
                <div className={styles.left}>
                  <img src={booking.imageUrl} alt={booking.name} />

                  <div className={styles.info}>
                    <p>{booking.name}</p>

                    <div className={styles.meta}>
                      <div>
                        <label>BOOKING ID</label>
                        <p>{shortId(booking.bookingId)}</p>
                      </div>
                      <div>
                        <label>DATE RANGE</label>
                        <p>{formatDateRange(booking.fromDate, booking.endDate)}</p>
                      </div>
                      <div>
                        <label>TOTAL COST</label>
                        <p>{formatCost(booking.cost)}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <div className={styles.right}>
                  {renderStatus(isCancelling ? "CANCELLING" : booking.status)}

                  {(booking.status?.toUpperCase() === "CONFIRMED" ||
                    booking.status?.toUpperCase() === "PENDING" ||
                    isCancelling) && (
                    <div className={styles.actions}>
                      {booking.status?.toUpperCase() === "CONFIRMED" && (
                        <button className={styles.receiptBtn}>View Receipt</button>
                      )}
                      <button
                        className={styles.cancelBtn}
                        onClick={() => handleCancel(booking.bookingId)}
                        disabled={isCancelling}
                      >
                        {isCancelling ? "Cancelling..." : "Cancel Booking"}
                      </button>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}