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
} from "react-icons/fa";
import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSearchAgencyById } from "../../Hooks/useSearchAgencyById";
import { useSearchParams } from "react-router-dom";

export default function AgencyPage() {
  const navigate = useNavigate();
  const { agency, loading, error, searchedAgency } = useSearchAgencyById();
  const [searchParams] = useSearchParams();
  const fromDate = searchParams.get("from");
  const toDate = searchParams.get("to");
  const { id } = useParams();

  useEffect(() => {
    console.log("AgencyPage mounted with ID:", id);
    const fetchAgencyDetails = async () => {
      await searchedAgency(id);
    };
    fetchAgencyDetails();
  }, [id]);


  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <section className={styles.hero}>
        <img src={agency?.agencyImage} alt={agency?.name} />

        <div className={styles.overlay}></div>

        <div className={styles.heroContent}>
          <div>
            <h1>{agency?.name}</h1>

            <p>
              <FaMapMarkerAlt />
              {agency?.address}
            </p>
          </div>

          <div className={styles.rating}>
            <FaStar />
            {agency?.rating} (1.2k Reviews)
          </div>
        </div>
      </section>

      <section className={styles.infoSection}>
        <div className={styles.aboutCard}>
          <h3>About the Agency</h3>

          <p>
            {agency?.details ||
              "No details available for this agency. Please check back later."}
          </p>

          <div className={styles.contactGrid}>
            <div>
              <span>
                <FaPhoneAlt />
              </span>

              <div>
                <label>PHONE</label>
                <p>{agency?.phone || "+44 20 7946 0123"}</p>
              </div>
            </div>

            <div>
              <span>
                <FaEnvelope />
              </span>

              <div>
                <label>EMAIL</label>
                <p>{agency?.email || "concierge@elitegrand.com"}</p>
              </div>
            </div>

            <div className={styles.addressRow}>
              <span>
                <FaMapMarkedAlt />
              </span>

              <div>
                <label>ADDRESS</label>
                <p>
                  {agency?.address ||
                    "12B Berkeley Square, Mayfair, London W1J 6EB, United Kingdom"}
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
        </div>

        <div className={styles.vehicleGrid}>
          {agency?.vehicleInfo?.map((vehicle, index) => (
            <div className={styles.vehicleCard} key={index}>
              <img src={vehicle.images[0]} alt={vehicle.carModel} />

              <div className={styles.vehicleContent}>
                <div className={styles.vehicleTop}>
                  <h3>{vehicle.name}</h3>

                  <span>{vehicle.carModel}</span>
                </div>

                <div className={styles.features}>
                  <p>{vehicle.features[0]}</p>

                  <p>{vehicle.features[1]}</p>
                </div>

                <div className={styles.vehicleBottom}>
                  <div>
                    <label>Price per km</label>
                    <h4>${vehicle.pricePerKm}</h4>
                  </div>

                  <button className={styles.bookBtn}
                    onClick={() => {
                      console.log("id:", id, "vehicleId:", vehicle.vehicleId, "from:", fromDate, "to:", toDate)
                      navigate(`/dashboard/agency/${id}/vehicle-details/${vehicle.vehicleId}?from=${fromDate}&to=${toDate}`)
                    }
                    }
                  >
                    Book Now
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      <section className={styles.reviewSection}>
        <h2>Customer Reviews</h2>

        <div className={styles.reviewGrid}>
          {agency?.reviews?.map((review, index) => (
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

              <p className={styles.reviewText}>"{review.text}"</p>

              <span className={styles.reviewDate}>{review.date}</span>
            </div>
          ))}
        </div>
      </section>

    </div>
  );
}