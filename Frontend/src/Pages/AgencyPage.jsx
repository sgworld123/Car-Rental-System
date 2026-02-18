import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useSearchAgencyById } from "../Hooks/useSearchAgencyById";
import { useNavigate } from "react-router-dom";

const AgencyPage = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const { agency, loading, error, searchedAgency } = useSearchAgencyById();
    const params = new URLSearchParams(location.search);

    const fromDate = params.get("from");
    const toDate = params.get("to");

    useEffect(() => {
        console.log("AgencyPage mounted with ID:", id);
        const fetchAgencyDetails = async () => {
            await searchedAgency(id);
        }
        fetchAgencyDetails();
    }, [id])
    const styles = {
        page: {
            minHeight: "100vh",
            background: "linear-gradient(135deg, #0f0f0f, #1a1a1a)",
            padding: "50px 20px",
            color: "#eaeaea",
            fontFamily: "Segoe UI, sans-serif",
        },
        container: {
            maxWidth: "1000px",
            margin: "0 auto",
        },
        card: {
            background: "rgba(25, 25, 25, 0.9)",
            borderRadius: "18px",
            padding: "35px",
            border: "1px solid rgba(255, 215, 0, 0.25)",
            boxShadow: "0 0 30px rgba(140, 0, 255, 0.15)",
            marginBottom: "40px",
        },
        title: {
            fontSize: "30px",
            color: "#b57cff",
            borderBottom: "2px solid #d4af37",
            paddingBottom: "12px",
            marginBottom: "25px",
            letterSpacing: "1px",
        },
        grid: {
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            gap: "20px",
        },
        field: {
            paddingBottom: "12px",
            borderBottom: "1px solid rgba(212,175,55,0.15)",
        },
        label: {
            fontSize: "13px",
            color: "#888",
            fontWeight: "600",
        },
        value: {
            fontSize: "16px",
            marginTop: "4px",
        },
        vehicleSectionTitle: {
            fontSize: "22px",
            marginBottom: "20px",
            color: "#d4af37",
        },
        vehicleGrid: {
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))",
            gap: "20px",
        },
        rentButton: {
            marginBottom: "12px",
            padding: "8px 16px",
            background: "linear-gradient(45deg, #7b2ff7, #b57cff)",
            border: "none",
            borderRadius: "8px",
            color: "white",
            fontWeight: "600",
            cursor: "pointer",
            transition: "all 0.3s ease",
        },
        vehicleCard: {
            background: "#141414",
            borderRadius: "14px",
            padding: "20px",
            border: "1px solid rgba(181,124,255,0.25)",
            transition: "all 0.3s ease",
            cursor: "pointer",
        },
        vehicleDetail: {
            fontSize: "14px",
            color: "#ccc",
            marginBottom: "6px"
        },
        vehicleLabel: {
            color: "#999",
            fontWeight: "600"
        },
        rating: {
            marginTop: "10px",
            color: "#d4af37",
            fontWeight: "600",
            fontSize: "15px"
        }
    };
    return (
  <div style={{ background: "#0f0f0f", minHeight: "100vh", padding: "40px 0" }}>
    <div style={{ maxWidth: "1100px", margin: "0 auto", padding: "0 24px" }}>

      {loading ? (
        <p style={{ color: "#b57cff" }}>Loading agency...</p>
      ) : error ? (
        <p style={{ color: "#ff4c4c" }}>Error loading agency</p>
      ) : agency && (
        <>
          {/* Agency Header */}
          <div style={{
            display: "flex",
            gap: "24px",
            marginBottom: "32px",
            background: "#1a1a1a",
            borderRadius: "20px",
            padding: "24px",
            border: "1px solid #2a2a2a"
          }}>
            <img
              src={agency.agencyImage}
              alt={agency.name}
              style={{
                width: "150px",
                height: "150px",
                borderRadius: "18px",
                objectFit: "cover"
              }}
            />

            <div style={{ flex: 1 }}>
              <h1 style={{ color: "#fff", marginBottom: "6px" }}>{agency.name}</h1>

              <p style={{ color: "#aaa", marginBottom: "10px" }}>
                {agency.details}
              </p>

              <div style={{
                display: "flex",
                gap: "20px",
                flexWrap: "wrap",
                color: "#bbb",
                fontSize: "14px"
              }}>
                <span>📍 {agency.address}</span>
                <span>📞 {agency.phone}</span>
                <span>✉️ {agency.email}</span>
                <span>⭐ {agency.rating}</span>
              </div>
            </div>
          </div>

          {/* Vehicles */}
          <h2 style={{ color: "#fff", marginBottom: "16px" }}>Available Vehicles</h2>

          <div style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill,minmax(260px,1fr))",
            gap: "20px",
            marginBottom: "40px"
          }}>
            {agency.vehicleInfo.map(vehicle => (
              <div
                key={vehicle.vehicleId}
                style={{
                  background: "#1a1a1a",
                  borderRadius: "16px",
                  overflow: "hidden",
                  border: "1px solid #2a2a2a"
                }}
              >
                <img
                  src={vehicle.coverImage}
                  alt={vehicle.carModel}
                  style={{
                    width: "100%",
                    height: "160px",
                    objectFit: "cover"
                  }}
                />

                <div style={{ padding: "16px" }}>
                  <h3 style={{ color: "#fff", marginBottom: "6px" }}>
                    {vehicle.carModel}
                  </h3>

                  <p style={{ color: "#aaa", marginBottom: "10px" }}>
                    ₹ {vehicle.pricePerKm} / km
                  </p>

                  <button
                    onClick={() =>
                      navigate(`/vehicle-details/${vehicle.vehicleId}?from=${fromDate}&to=${toDate}`)
                    }
                    style={{
                      width: "100%",
                      padding: "10px",
                      borderRadius: "10px",
                      border: "none",
                      background: "linear-gradient(135deg,#6d28d9,#9333ea)",
                      color: "#fff",
                      cursor: "pointer"
                    }}
                  >
                    Book Now
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Reviews */}
          <h2 style={{ color: "#fff", marginBottom: "16px" }}>Customer Reviews</h2>

          {agency.reviews?.length ? (
            <div style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill,minmax(300px,1fr))",
              gap: "16px"
            }}>
              {agency.reviews.map((review, idx) => (
                <div
                  key={idx}
                  style={{
                    background: "#1a1a1a",
                    borderRadius: "14px",
                    padding: "16px",
                    border: "1px solid #2a2a2a"
                  }}
                >
                  <div style={{ color: "#b57cff", marginBottom: "6px" }}>
                    ⭐ {review.rating}
                  </div>

                  <p style={{ color: "#ddd", fontSize: "14px" }}>
                    {review.reviewText}
                  </p>

                  <div style={{ color: "#777", fontSize: "12px", marginTop: "8px" }}>
                    User: {review.userId}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p style={{ color: "#888" }}>No reviews yet.</p>
          )}
        </>
      )}
    </div>
  </div>
);

}
export default AgencyPage;