import styles from "./VehicleDetails.module.css";
import { useState, useEffect } from "react";
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
  FaArrowLeft,
  FaMapMarkerAlt,
  FaShieldAlt,
} from "react-icons/fa";

import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useVehicleById } from "../../Hooks/useVehicleById";
import { useSearchParams } from "react-router-dom";
import { useCreateBooking } from "../../Hooks/useCreateBooking";
import BookingModal from "../BookingModal/BookingModal";

export default function VehicleDetails() {
  const navigate = useNavigate();
  const { vehicleId } = useParams();
  const location = useLocation();

  const { vehicle, loading, error, fetchVehicle } = useVehicleById(vehicleId);

  useEffect(() => {
    fetchVehicle();
  }, [vehicleId]);

  const agency = location.state?.agency || {};

  const [activeImg, setActiveImg] = useState(0);
  const [wished, setWished] = useState(false);
  const [searchParams] = useSearchParams();
  const fromDate = searchParams.get("from");
  const toDate = searchParams.get("to");
  const [bookingLoading, setBookingLoading] = useState(false);

  const { handleCreateBooking } = useCreateBooking();


  const calcDays = () => {
    if (!fromDate || !toDate) return 1;
    const diff = new Date(toDate) - new Date(fromDate);
    return Math.max(1, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  };
  const days = calcDays();

  const [driverOption, setDriverOption] = useState("self"); // "self" | "driver"
  const DRIVER_FEE_PER_DAY = 500;
  const SERVICE_FEE = 250;
  const KM = 100;

  const kmCost = vehicle?.pricePerKm * KM * days;
  const driverCost = driverOption === "driver" ? DRIVER_FEE_PER_DAY * days : 0;
  const total = kmCost + driverCost + SERVICE_FEE;
  const [showModal, setShowModal] = useState(false);

  const handleBook = async () => {
    if (!fromDate || !toDate) {
      alert("Search dates missing");
      return;
    }
    try {
      setBookingLoading(true);
      const payload = { vehicleId, total, fromDate, toDate };
      const result = await handleCreateBooking(payload);
      setPendingBookingId(result.bookingId);
      setShowModal(true);
    } catch (error) {
      console.error("Booking creation failed:", error);
      alert("Failed to initiate booking. Please try again.");
    } finally {
      setBookingLoading(false);
    }
  };

  /* ── Loading / Error / Empty ── */
  if (loading) {
    return (
      <div className={styles.page}>
        <div className={styles.stateScreen}>
          <div className={styles.spinner} />
          <p>Loading vehicle details…</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div className={styles.stateScreen}>
          <p className={styles.errorText}>Failed to load vehicle.</p>
          <button className={styles.retryBtn} onClick={() => navigate(-1)}>
            Go Back
          </button>
        </div>
      </div>
    );
  }

  if (!vehicle) return null;

  const specs = [
    { icon: <FaBolt />, label: "Fuel Type", value: vehicle.features?.[1] || "—" },
    { icon: <FaTachometerAlt />, label: "Car Type", value: vehicle.carModel || "—" },
    { icon: <FaSlidersH />, label: "Transmission", value: vehicle.features?.[0] || "—" },
    { icon: <FaChair />, label: "Driver", value: vehicle.driverName || "—" },
  ];

  return (
    <div className={styles.page}>
      {/* Warm ambient background */}
      <div className={styles.grid} />
      {/* ── MAIN ── */}
      <main className={styles.main}>

        {/* ═══ LEFT COLUMN ═══ */}
        <div className={styles.left}>

          {/* Image Carousel */}
          <div className={styles.carousel}>
            <div className={styles.mainImgWrap}>
              <img
                src={vehicle.images?.[activeImg] || vehicle.coverImage}
                alt={vehicle.name}
                className={styles.mainImg}
              />
              <span className={styles.imgCounter}>
                {activeImg + 1} / {vehicle.images?.length || 1}
              </span>
            </div>

            <div className={styles.thumbRow}>
              {vehicle.images?.map((img, i) => (
                <button
                  key={i}
                  className={`${styles.thumb} ${i === activeImg ? styles.thumbActive : ""}`}
                  onClick={() => setActiveImg(i)}
                >
                  <img src={img} alt={`vehicle-${i}`} />
                </button>
              ))}
            </div>
          </div>

          {/* Title Block */}
          <div className={styles.titleBlock}>
            <div className={styles.titleRow}>
              <div>
                <div className={styles.badgeRow}>
                  <span className={styles.badge}>{vehicle.carModel}</span>
                  <span className={styles.carNumber}>
                    <FaShieldAlt />
                    {vehicle.carNumber}
                  </span>
                </div>
                <h1 className={styles.carName}>{vehicle.name}</h1>
              </div>

              <div className={styles.ratingPill}>
                <FaStar />
                <span>4.9</span>
                <em>({vehicle.reviews?.length || 0} reviews)</em>
              </div>
            </div>

            <p className={styles.description}>{vehicle.description}</p>

            {agency?.name && (
              <div className={styles.agencyTag}>
                <FaMapMarkerAlt />
                <span>{agency.name} · {agency.address}</span>
              </div>
            )}
          </div>

          {/* Specs */}
          <div className={styles.specGrid}>
            {specs.map((s, i) => (
              <div key={i} className={styles.specCard}>
                <div className={styles.specIcon}>{s.icon}</div>
                <div>
                  <label>{s.label}</label>
                  <h4>{s.value}</h4>
                </div>
              </div>
            ))}
          </div>

          {/* Features */}
          <div className={styles.featuresSection}>
            <h2 className={styles.sectionTitle}>Features & Amenities</h2>
            <div className={styles.featuresGrid}>
              {vehicle.features?.map((feat, i) => (
                <p key={i}>
                  <FaCheckCircle />
                  {feat}
                </p>
              ))}
              <p><FaCheckCircle />GPS Navigation</p>
              <p><FaCheckCircle />Climate Control</p>
            </div>
          </div>

          {/* Reviews */}
          <div className={styles.reviewSection}>
            <h2 className={styles.sectionTitle}>Guest Reviews</h2>
            <div className={styles.reviewList}>
              {vehicle.reviews?.map((rev, i) => (
                <div key={i} className={styles.reviewCard}>
                  <div className={styles.reviewHeader}>
                    <div className={styles.reviewUser}>
                      <img src={rev.avatar} alt={rev.name} />
                      <div>
                        <h4>{rev.name}</h4>
                        <span>{rev.date}</span>
                      </div>
                    </div>
                    <div className={styles.reviewStars}>
                      {[...Array(5)].map((_, s) => <FaStar key={s} />)}
                    </div>
                  </div>
                  <p className={styles.reviewText}>{rev.text}</p>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* ═══ RIGHT COLUMN — BOOKING CARD ═══ */}
        <aside className={styles.bookingCard}>
          <div className={styles.priceRow}>
            <div className={styles.priceBlock}>
              <h2>₹{vehicle.pricePerKm}</h2>
              <span>/ km</span>
            </div>
            <p className={styles.taxNote}>
              <FaCog />
              Inclusive of tax
            </p>
          </div>

          <div className={styles.divider} />

          <div className={styles.field}>
            <label>Trip Dates</label>
            <div className={styles.inputRow}>
              <FaCalendarAlt />
              <span>{fromDate} to {toDate}</span>
            </div>
          </div>

          <div className={styles.field}>
            <label>Driver option</label>
            <div className={styles.inputRow}>
              <FaUser />
              <select
                value={driverOption}
                onChange={(e) => setDriverOption(e.target.value)}
                className={styles.driverSelect}
              >
                <option value="self">Self drive</option>
                <option value="driver">
                  {vehicle.driverName
                    ? `With driver (${vehicle.driverName}) +₹${DRIVER_FEE_PER_DAY}/day`
                    : `With driver +₹${DRIVER_FEE_PER_DAY}/day`}
                </option>
              </select>
            </div>
          </div>

          <div className={styles.divider} />

          <div className={styles.bill}>
            <div className={styles.billRow}>
              <span>Est. 100 km × {days} {days === 1 ? "day" : "days"}</span>
              <span>₹{kmCost.toLocaleString('en-IN')}</span>
            </div>
            {driverOption === "driver" && (
              <div className={styles.billRow}>
                <span>Driver fee ({days} days)</span>
                <span>₹{driverCost.toLocaleString('en-IN')}</span>
              </div>
            )}
            <div className={styles.billRow}>
              <span>Service fee</span>
              <span>₹250</span>
            </div>
          </div>

          <div className={styles.totalRow}>
            <span>Total</span>
            <h2>₹{total.toLocaleString('en-IN')}</h2>
          </div>

          <button className={styles.bookBtn} onClick={() => {
            handleBook();
            setShowModal(true);
          }
          }>Confirm Booking</button>

          {showModal && (
            <BookingModal
              vehicle={vehicle}
              fromDate={fromDate}
              toDate={toDate}
              days={days}
              driverOption={driverOption}
              onClose={() => setShowModal(false)}
            />
          )}

          <p className={styles.note}>You won't be charged yet</p>
        </aside>
      </main>
    </div>
  );
}