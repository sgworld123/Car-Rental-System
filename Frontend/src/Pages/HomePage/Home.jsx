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
import { useSearchParams } from "react-router-dom";


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

      <div className={styles.searchResults}>
        <p className={styles.sectionHeader}>Searched Results :</p>
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
        ) : <div className={`${styles.agencyList} ${styles.centreCard}`}>
          {searchResults.map((agency) => (
            <div className={styles.card} key={agency.id} onClick={() => {
              const { fromDate, toDate } = tripData;
              navigate(`/dashboard/agency/${agency.id}?from=${fromDate}&to=${toDate}`)
            }}>
              <div className={styles.cardLeft}>
                <img src={agency.agencyImage} alt={agency.name} />
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
        </div>}
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