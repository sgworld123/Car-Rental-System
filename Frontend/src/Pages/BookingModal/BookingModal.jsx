import styles from "./BookingModal.module.css";
import { FaCar, FaCalendarAlt, FaClock, FaUser, FaTimes, FaCheckCircle } from "react-icons/fa";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useConfirmBooking } from "../../Hooks/useConfirmBooking";

export default function BookingModal({ vehicle, fromDate, toDate, days, driverOption, onClose, bookingId }) {
  const navigate = useNavigate();
  const [confirmed, setConfirmed] = useState(false);

  const DRIVER_FEE_PER_DAY = 500;
  const SERVICE_FEE = 250;
  const KM = 100;

  const kmCost = vehicle.pricePerKm * KM * days;
  const driverCost = driverOption === "driver" ? DRIVER_FEE_PER_DAY * days : 0;
  const total = kmCost + driverCost + SERVICE_FEE;
  const [bookingLoading, setBookingLoading] = useState(false);
  const { handleConfirmBooking } = useConfirmBooking();

  const confirmBooking = async () => {
    try {
      setBookingLoading(true);
      await handleConfirmBooking(bookingId);
      navigate("/dashboard/bookings");
    } catch (error) {
      console.error("Confirm failed:", error);
      alert("Failed to confirm booking. Please try again.");
    } finally {
      setBookingLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        {!confirmed ? (
          <>
            <div className={styles.header}>
              <h2>Booking summary</h2>
              <button className={styles.closeBtn} onClick={onClose} aria-label="Close">
                <FaTimes />
              </button>
            </div>

            <div className={styles.body}>
              {/* Vehicle */}
              <p className={styles.sectionLabel}>Vehicle</p>
              <div className={styles.vehicleRow}>
                <div className={styles.vehicleIcon}><FaCar /></div>
                <div>
                  <p className={styles.vehicleName}>{vehicle.name}</p>
                  <p className={styles.vehicleSub}>{vehicle.carModel} · {vehicle.carNumber}</p>
                </div>
              </div>

              {/* Trip details */}
              <p className={styles.sectionLabel}>Trip details</p>
              <div className={styles.infoBox}>
                <div className={styles.infoRow}>
                  <FaCalendarAlt /><span className={styles.lbl}>Check-in</span>
                  <span className={styles.val}>{fromDate}</span>
                </div>
                <div className={styles.infoRow}>
                  <FaCalendarAlt /><span className={styles.lbl}>Check-out</span>
                  <span className={styles.val}>{toDate}</span>
                </div>
                <div className={styles.infoRow}>
                  <FaClock /><span className={styles.lbl}>Duration</span>
                  <span className={styles.val}>{days} {days === 1 ? "day" : "days"}</span>
                </div>
                <div className={styles.infoRow}>
                  <FaUser /><span className={styles.lbl}>Driver</span>
                  <span className={styles.val}>
                    {driverOption === "driver" ? (vehicle.driverName || "With driver") : "Self drive"}
                  </span>
                </div>
              </div>

              {/* Breakdown */}
              <p className={styles.sectionLabel}>Price breakdown</p>
              <div className={styles.billBox}>
                <div className={styles.billRow}>
                  <span>100 km × {days} days @ ₹{vehicle.pricePerKm}/km</span>
                  <span>₹{kmCost.toLocaleString("en-IN")}</span>
                </div>
                {driverOption === "driver" && (
                  <div className={styles.billRow}>
                    <span>Driver fee ({days} days × ₹{DRIVER_FEE_PER_DAY})</span>
                    <span>₹{driverCost.toLocaleString("en-IN")}</span>
                  </div>
                )}
                <div className={styles.billRow}>
                  <span>Service fee</span>
                  <span>₹{SERVICE_FEE}</span>
                </div>
                <div className={`${styles.billRow} ${styles.total}`}>
                  <span>Total</span>
                  <span>₹{total.toLocaleString("en-IN")}</span>
                </div>
              </div>

              <button className={styles.payBtn} onClick={confirmBooking} disabled={bookingLoading}>
                {bookingLoading ? "Confirming..." : `Pay ₹${total.toLocaleString("en-IN")}`}
              </button>
              <p className={styles.note}>Secure payment · You won't be charged until confirmed</p>
            </div>
          </>
        ) : (
          <div className={styles.success}>
            <FaCheckCircle className={styles.successIcon} />
            <h3>Booking confirmed!</h3>
            <p>Your booking has been placed successfully.<br />Check your email for details.</p>
          </div>
        )}
      </div>
    </div>
  );
}