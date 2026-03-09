import React, { useState } from 'react';
import { useSearchAgencies } from '../Hooks/useSearchAgencies';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const { searchResults, searchAgency } = useSearchAgencies();
  const [hit, setHit] = useState(false);
  const navigate = useNavigate();
  const [tripData, setTripData] = useState({
    sourceCity: '',
    destinationCity: '',
    fromDate: '',
    toDate: '',
    pageNumber: '1'
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

  const styles = {
    main: {
      backgroundColor: '#121212',
      minHeight: 'calc(100vh - 140px)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      padding: '60px 20px',
      color: '#fff',
      fontFamily: 'Arial, sans-serif',
    },
    quoteContainer: {
      textAlign: 'center',
      marginBottom: '50px',
      maxWidth: '800px',
    },
    quoteText: {
      fontSize: '32px',
      fontWeight: '300',
      fontStyle: 'italic',
      color: '#bb86fc', // Light purple accent
      lineHeight: '1.4',
    },
    quoteAuthor: {
      marginTop: '15px',
      fontSize: '16px',
      color: '#b0b0b0',
      letterSpacing: '2px',
      textTransform: 'uppercase',
    },
    formCard: {
      backgroundColor: '#1e1e1e',
      padding: '30px',
      borderRadius: '12px',
      border: '1px solid #333',
      width: '100%',
      maxWidth: '900px',
      boxShadow: '0 10px 30px rgba(0,0,0,0.5)',
    },
    formRow: {
      display: 'flex',
      flexWrap: 'wrap',
      gap: '20px',
      alignItems: 'flex-end',
    },
    inputGroup: {
      flex: '1',
      minWidth: '200px',
      display: 'flex',
      flexDirection: 'column',
      gap: '8px',
    },
    label: {
      fontSize: '14px',
      color: '#b0b0b0',
      fontWeight: '600',
    },
    input: {
      padding: '12px',
      borderRadius: '6px',
      border: '1px solid #444',
      backgroundColor: '#2c2c2c',
      color: '#fff',
      fontSize: '15px',
      outline: 'none',
    },
    searchButton: {
      backgroundColor: '#6200ea',
      color: 'white',
      border: 'none',
      padding: '12px 30px',
      borderRadius: '6px',
      fontSize: '16px',
      fontWeight: 'bold',
      cursor: 'pointer',
      transition: 'transform 0.2s, background-color 0.2s',
      height: '45px',
    }
  };

  return (
    <main style={styles.main}>
      <div style={styles.quoteContainer}>
        <p style={styles.quoteText}>
          "The world is a book and those who do not travel read only one page."
        </p>
        <p style={styles.quoteAuthor}>— Saint Augustine</p>
      </div>

      <div style={styles.formCard}>
        <form style={styles.formRow} onSubmit={handleSubmit}>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Source</label>
            <input
              style={styles.input}
              type="text"
              name="sourceCity"
              placeholder="Leaving from..."
              value={tripData.sourceCity}
              onChange={handleChange}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Destination</label>
            <input
              style={styles.input}
              type="text"
              name="destinationCity"
              placeholder="Going to..."
              value={tripData.destinationCity}
              onChange={handleChange}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Start Date</label>
            <input
              style={styles.input}
              type="date"
              name="fromDate"
              value={tripData.fromDate}
              onChange={handleChange}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>To Date</label>
            <input
              style={styles.input}
              type="date"
              name="toDate"
              value={tripData.toDate}
              onChange={handleChange}
              required
            />
          </div>
          <button
            type="submit"
            style={styles.searchButton}
            onMouseEnter={(e) => e.target.style.backgroundColor = '#3700b3'}
            onMouseLeave={(e) => e.target.style.backgroundColor = '#6200ea'}
          >
            Find Trips
          </button>
        </form>
      </div>
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



    </main>
  );
};

export default Home;