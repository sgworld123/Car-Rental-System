import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useSearchAgencyById } from "../Hooks/useSearchAgencyById";
import { useNavigate } from "react-router-dom";

const AgencyPage = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const { agency, loading, error, searchedAgency } = useSearchAgencyById();

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
    const handleRent = (vehicle) => {
        console.log("Renting vehicle:", vehicle.vehicleId);
        navigate("/confirm-booking", { state: { vehicle } });
    }

    return (
        <div style={styles.page}>
            <div style={styles.container}>

                {loading ? (
                    <p style={{ color: "#b57cff" }}>Loading agency details...</p>
                ) : error ? (
                    <p style={{ color: "#ff4c4c" }}>Error loading agency details</p>
                ) : agency ? (
                    <>
                        {/* Agency Info Card */}
                        <div style={styles.card}>
                            <h1 style={styles.title}>{agency.name}</h1>

                            <div style={styles.grid}>
                                <div style={styles.field}>
                                    <div style={styles.label}>Email</div>
                                    <div style={styles.value}>{agency.email}</div>
                                </div>

                                <div style={styles.field}>
                                    <div style={styles.label}>Phone</div>
                                    <div style={styles.value}>{agency.phone}</div>
                                </div>

                                <div style={styles.field}>
                                    <div style={styles.label}>Source City</div>
                                    <div style={styles.value}>{agency.sourceCity}</div>
                                </div>

                                <div style={styles.field}>
                                    <div style={styles.label}>Address</div>
                                    <div style={styles.value}>{agency.address}</div>
                                </div>
                            </div>
                        </div>

                        {/* Vehicles Section */}
                        <div>
                            <h2 style={styles.vehicleSectionTitle}>Available Vehicles</h2>

                            <div style={styles.vehicleGrid}>
                                {agency.vehicleInfo?.length > 0 ? (
                                    agency.vehicleInfo.map((vehicle) => (
                                        <div
                                            key={vehicle.vehicleId}
                                            style={styles.vehicleCard}
                                            onMouseEnter={(e) =>
                                            (e.currentTarget.style.boxShadow =
                                                "0 0 20px rgba(181,124,255,0.4)")
                                            }
                                            onMouseLeave={(e) =>
                                                (e.currentTarget.style.boxShadow = "none")
                                            }
                                        >
                                            <h3 style={{ color: "#b57cff", marginBottom: "10px" }}>
                                                {vehicle.carModel} Car
                                            </h3>

                                            <button
                                                style={styles.rentButton}
                                                onClick={() => handleRent(vehicle)}
                                            >
                                                Book Now
                                            </button>
                                            <div style={styles.vehicleDetail}>
                                                <span style={styles.vehicleLabel}>Vehicle ID:</span>{" "}
                                                {vehicle.vehicleId}
                                            </div>

                                            <div style={styles.vehicleDetail}>
                                                <span style={styles.vehicleLabel}>Price per Km:</span>{" "}
                                                ₹ {vehicle.pricePerKm}
                                            </div>

                                            <div style={styles.rating}>
                                                ⭐ {vehicle.rating}
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <p style={{ color: "#888" }}>No vehicles available.</p>
                                )}
                            </div>
                        </div>
                    </>
                ) : (
                    <p style={{ color: "#888" }}>No agency found.</p>
                )}

            </div>
        </div>
    );


}
export default AgencyPage;