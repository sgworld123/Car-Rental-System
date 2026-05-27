import styles from "./Home.module.css";
import { useState } from "react";
import { useSearchAgencies } from "../../Hooks/useSearchAgencies";
import { useNavigate } from "react-router-dom";

import {
  FaMapMarkerAlt,
  FaTelegramPlane,
  FaCalendarAlt,
  FaSearch,
  FaArrowRight,
  FaPhoneAlt,
  FaEnvelope,
  FaStar,
} from "react-icons/fa";

export default function Home() {
  const { searchResults, searchAgency } = useSearchAgencies();
  const [hit, setHit] = useState(false);
  const navigate = useNavigate();
  const [tripData, setTripData] = useState({
    sourceCity: '',
    destinationCity: '',
    fromDate: '',
    toDate: '',
    pageNumber: 0,
    pageSize: 10
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTripData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Trip Search:', tripData);
    setHit(true);
    await searchAgency(tripData);
  };
  const agencies = [
    {
      id: 1,
      name: "Premium Rentals Co.",
      rating: "4.9",
      reviews: "120+",
      address: "123 Main St, Downtown Hub",
      phone: "+1 555-0199",
      email: "contact@premiumrentals.com",
      image:
        "https://images.unsplash.com/photo-1494526585095-c41746248156?q=80&w=400&auto=format&fit=crop",
    },
    {
      id: 2,
      name: "Velocity Drive",
      rating: "4.7",
      reviews: "85+",
      address: "45 Airport Road, Terminal B",
      phone: "+1 555-0244",
      email: "hello@velocity.co",
      image:
        "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=400&auto=format&fit=crop",
    },
  ];

  return (
    <div className={styles.page}>
      <div className={styles.grid}></div>

      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1>Your journey starts here.</h1>

          <p>
            Discover the freedom of the open road with DriveEasy.
            Premium vehicles, seamless booking, and local expertise
            at your fingertips.
          </p>
        </div>

        <div className={styles.pattern}>
          <div></div>
        </div>
      </section>

      <section className={styles.searchBox}>
        <div className={styles.searchGrid}>
          <div className={styles.inputGroup}>
            <label>
              <FaMapMarkerAlt />
              Source City
            </label>

            <input type="text" 
            placeholder="Where from?" 
            name="sourceCity" 
            value={tripData.sourceCity} 
            onChange={handleChange} />
          </div>

          <div className={styles.inputGroup}>
            <label>
              <FaTelegramPlane />
              Destination City
            </label>

            <input
              type="text"
              name="destinationCity"
              placeholder="Where to?"
              value={tripData.destinationCity}
              onChange={handleChange}
            />
          </div>

          <div className={styles.inputGroup}>
            <label>
              <FaCalendarAlt />
              Start Date
            </label>

            <input
              type="date"
              name="fromDate"
              value={tripData.fromDate}
              onChange={handleChange}
            />
          </div>

          <div className={styles.inputGroup}>
            <label>
              <FaCalendarAlt />
              End Date
            </label>

            <input
              type="date"
              name="toDate"
              value={tripData.toDate}
              onChange={handleChange}
            />
          </div>

          <button className={styles.searchBtn} onClick={handleSubmit}>
            <FaSearch />
            Search
          </button >
        </div>
      </section>

      <div
        style={{
          marginTop: '20px',
          width: '100%',
          maxWidth: '900px',
          display: hit ? 'block' : 'none',
          background: '#1a1a1a',
          borderRadius: '16px',
          padding: '24px',
          boxShadow: '0 8px 32px rgba(0,0,0,0.4)',
          cursor: 'pointer'
        }}
       >
        {searchResults.length === 0 ? (
          <p style={{
            textAlign: 'center',
            color: '#8a8a8a',
            fontSize: '16px',
            padding: '24px 0',
            margin: 0
          }}>
            No search results found.
          </p>
        ) : (
          searchResults.map((agency, index) => (
            <div
              key={index}
              className="agency-card"
              style={{
                background: "#1f1f1f",
                border: "1px solid #2f2f2f",
                borderRadius: "16px",
                padding: "20px",
                marginBottom: "18px",
                transition: "all 0.3s ease",
                cursor: "pointer",
                display: "flex",
                gap: "20px",
                alignItems: "center"
              }}
              onClick={() => {
                const { fromDate, toDate } = tripData;
                navigate(`/agency/${agency.id}?from=${fromDate}&to=${toDate}`);
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = "translateY(-4px)";
                e.currentTarget.style.boxShadow =
                  "0 15px 30px rgba(0,0,0,0.6)";
                e.currentTarget.style.borderColor = "#3f3f3f";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = "translateY(0)";
                e.currentTarget.style.boxShadow = "none";
                e.currentTarget.style.borderColor = "#2f2f2f";
              }}
            >
              {/* Agency Image */}
              <img
                src={agency.agencyImage}
                alt={agency.name}
                style={{
                  width: "90px",
                  height: "90px",
                  borderRadius: "14px",
                  objectFit: "cover",
                  flexShrink: 0
                }}
              />

              {/* Content Section */}
              <div style={{ flex: 1 }}>
                <h3
                  style={{
                    margin: "0 0 6px 0",
                    color: "#ffffff",
                    fontSize: "22px",
                    fontWeight: "600"
                  }}
                >
                  {agency.name}
                </h3>

                <p
                  style={{
                    margin: "0 0 10px 0",
                    color: "#9a9a9a",
                    fontSize: "14px"
                  }}
                >
                  {agency.address}
                </p>

                <div
                  style={{
                    display: "flex",
                    gap: "20px",
                    alignItems: "center",
                    fontSize: "14px",
                    color: "#b0b0b0"
                  }}
                >
                  <span>📞 {agency.phone}</span>
                  <span>⭐ {agency.rating}</span>
                </div>
              </div>

              {/* CTA Section */}
              <div style={{ textAlign: "right" }}>
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    window.open(`mailto:${agency.email}`);
                  }}
                  style={{
                    padding: "10px 18px",
                    background: "linear-gradient(135deg, #6d28d9, #9333ea)",
                    border: "none",
                    borderRadius: "10px",
                    color: "white",
                    fontWeight: "500",
                    cursor: "pointer",
                    transition: "all 0.2s ease"
                  }}
                  onMouseEnter={(e) => {
                    e.target.style.transform = "translateY(-2px)";
                  }}
                  onMouseLeave={(e) => {
                    e.target.style.transform = "translateY(0)";
                  }}
                >
                  Contact
                </button>
              </div>
            </div>
          ))

        )}
      </div>

      <section className={styles.agencies}>
        <div className={styles.sectionHeader}>
          <h2>Top Agencies Near You</h2>

        </div>

        <div className={styles.agencyList}>
          {agencies.map((agency) => (
            <div className={styles.card} key={agency.id}>
              <div className={styles.cardLeft}>
                <img src={agency.image} alt={agency.name} />

                <div className={styles.cardInfo}>
                  <div className={styles.titleRow}>
                    <h3>{agency.name}</h3>

                    <div className={styles.rating}>
                      <FaStar />
                      <span>
                        {agency.rating} ({agency.reviews})
                      </span>
                    </div>
                  </div>

                  <p className={styles.address}>
                    <FaMapMarkerAlt />
                    {agency.address}
                  </p>

                  <div className={styles.contact}>
                    <span>
                      <FaPhoneAlt />
                      {agency.phone}
                    </span>

                    <span>
                      <FaEnvelope />
                      {agency.email}
                    </span>
                  </div>
                </div>
              </div>

              <button className={styles.viewBtn}>
                View Agency
                <FaArrowRight />
              </button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}