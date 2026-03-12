import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useVehicleById } from '../Hooks/useVehicleById';
import { useCreateBooking } from '../Hooks/useCreateBooking';
import { useConfirmBooking } from '../Hooks/useConfirmBooking';

const VehicleDetails = () => {
    const location = useLocation();
    const { vehicleId } = useParams();
    const navigate = useNavigate();

    const [showModal, setShowModal] = useState(false);
    const [currentImage, setCurrentImage] = useState(0);


    const { vehicle, loading, error, fetchVehicle } = useVehicleById(vehicleId);

    const { handleCreateBooking } = useCreateBooking();
    const { handleConfirmBooking } = useConfirmBooking();

    // store bookingId returned from createBooking
    const [pendingBookingId, setPendingBookingId] = useState(null);
    const [bookingLoading, setBookingLoading] = useState(false);

    const params = new URLSearchParams(location.search);
    const fromDate = params.get("from");
    const toDate = params.get("to");


    useEffect(() => {
        fetchVehicle();
    }, [vehicleId]);

    const handleBook = async () => {
        if (!fromDate || !toDate) {
            alert("Search dates missing");
            return;
        }
        try {
            setBookingLoading(true);
            const payload = { vehicleId, cost, fromDate, toDate };
            const result = await handleCreateBooking(payload);
            setPendingBookingId(result.bookingId); // ✅ save bookingId from response
            setShowModal(true);
        } catch (error) {
            console.error("Booking creation failed:", error);
            alert("Failed to initiate booking. Please try again.");
        } finally {
            setBookingLoading(false);
        }
    };

    // Confirm → calls confirmBooking with saved bookingId
    const confirmBooking = async () => {
        try {
            setBookingLoading(true);
            await handleConfirmBooking(pendingBookingId); // ✅ uses bookingId from createBooking
            setShowModal(false);
            navigate("/dashboard/bookings");
        } catch (error) {
            console.error("Confirm failed:", error);
            alert("Failed to confirm booking. Please try again.");
        } finally {
            setBookingLoading(false);
        }
    };

    const days =
        fromDate && toDate
            ? Math.ceil((new Date(toDate) - new Date(fromDate)) / (1000 * 60 * 60 * 24))
            : 0;

    const cost = vehicle && days > 0 ? vehicle.pricePerKm * days : 0;

    return (
        <div style={{ background: "#0f0f0f", minHeight: "100vh", padding: "40px 0" }}>

            {loading && <p style={{ color: "#aaa", textAlign: "center" }}>Loading...</p>}
            {error && <p style={{ color: "red", textAlign: "center" }}>{error}</p>}

            {vehicle && (() => {
                const images = vehicle.images?.length
                    ? vehicle.images
                    : [vehicle.coverImage];

                return (
                    <div style={{ maxWidth: "1100px", margin: "0 auto", padding: "0 24px" }}>
                        {/* Close */}
                            <span
                                onClick={() => navigate(-1)}
                                style={{
                                    
                                    top: "14px",
                                    right: "16px",
                                    color: "#aaa",
                                    cursor: "pointer",
                                    fontSize: "18px"
                                }}
                            >
                                ✕
                            </span>

                        {/* Slider */}
                        <div style={{ position: "relative", width: "100%", height: "380px", marginBottom: "24px" }}>
                            <img
                                src={images[currentImage]}
                                alt="car"
                                style={{ width: "100%", height: "100%", objectFit: "cover", borderRadius: "20px" }}
                            />
                            {/* Left */}
                            <button
                                onClick={() =>
                                    setCurrentImage(currentImage === 0 ? images.length - 1 : currentImage - 1)
                                }
                                style={{
                                    position: "absolute", top: "50%", left: "15px", transform: "translateY(-50%)", background: "rgba(0,0,0,.6)",
                                    border: "none",
                                    color: "#fff",
                                    fontSize: "22px",
                                    padding: "8px 12px",
                                    borderRadius: "8px",
                                    cursor: "pointer"
                                }}>
                                ‹
                            </button>
                            {/* Right */}
                            <button
                                onClick={() =>
                                    setCurrentImage(currentImage === images.length - 1 ? 0 : currentImage + 1)
                                }
                                style={{
                                    position: "absolute",
                                    top: "50%",
                                    right: "15px",
                                    transform: "translateY(-50%)",
                                    background: "rgba(0,0,0,.6)",
                                    border: "none",
                                    color: "#fff",
                                    fontSize: "22px",
                                    padding: "8px 12px",
                                    borderRadius: "8px",
                                    cursor: "pointer"
                                }}
                            >
                                ›
                            </button>

                            {/* Dots */}
                            <div style={{
                                position: "absolute",
                                bottom: "12px",
                                left: "50%",
                                transform: "translateX(-50%)",
                                display: "flex",
                                gap: "6px"
                            }}>
                                {images.map((_, i) => (
                                    <div
                                        key={i}
                                        onClick={() => setCurrentImage(i)}
                                        style={{
                                            width: "8px",
                                            height: "8px",
                                            borderRadius: "50%",
                                            background: currentImage === i ? "#b57cff" : "#666",
                                            cursor: "pointer"
                                        }}
                                    />
                                ))}
                            </div>
                        </div>

                        <div style={{ display: "grid", gridTemplateColumns: "2fr 1fr", gap: "30px" }}>

                            {/* Left */}
                            <div>
                                <h1 style={{ color: "#fff", marginBottom: "10px" }}>
                                    {vehicle.carModel}
                                </h1>

                                <p style={{ color: "#aaa", marginBottom: "20px" }}>
                                    {vehicle.description}
                                </p>

                                <h2 style={{ color: "#fff", marginBottom: "12px" }}>Reviews</h2>

                                {vehicle.reviews?.length ? vehicle.reviews.map((r, i) => (
                                    <div key={i} style={{
                                        background: "#1a1a1a",
                                        padding: "14px",
                                        borderRadius: "12px",
                                        marginBottom: "10px",
                                        border: "1px solid #2a2a2a"
                                    }}>
                                        <div style={{ color: "#b57cff" }}>⭐ {r.rating}</div>
                                        <p style={{ color: "#ddd", fontSize: "14px" }}>{r.reviewText}</p>
                                        <span style={{ color: "#777", fontSize: "12px" }}>{r.userId}</span>
                                    </div>
                                )) : (
                                    <p style={{ color: "#777" }}>No reviews yet.</p>
                                )}
                            </div>

                            {/* Booking Card */}
                            <div style={{
                                background: "#1a1a1a",
                                borderRadius: "18px",
                                padding: "20px",
                                border: "1px solid #2a2a2a",
                                height: "fit-content"
                            }}>
                                <h2 style={{ color: "#fff", marginBottom: "12px" }}>
                                    ₹ {vehicle.pricePerKm} / km
                                </h2>

                                <p style={{ color: "#aaa", marginBottom: "20px" }}>
                                    Driver: {vehicle.driverName}
                                </p>

                                <button
                                    onClick={handleBook}
                                    style={{
                                        width: "100%",
                                        padding: "12px",
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
                    </div>
                );
            })()}

            {/* Modal */}
            {showModal && vehicle && (
                <div style={{
                    position: "fixed",
                    inset: 0,
                    background: "rgba(0,0,0,.75)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    zIndex: 1000
                }}>
                    <div style={{
                        background: "#141414",
                        padding: "24px",
                        borderRadius: "20px",
                        width: "380px",
                        border: "1px solid #2a2a2a",
                        position: "relative"
                    }}>

                        {/* Close */}
                        <span
                            onClick={() => setShowModal(false)}
                            style={{
                                position: "absolute",
                                top: "14px",
                                right: "16px",
                                color: "#aaa",
                                cursor: "pointer",
                                fontSize: "18px"
                            }}
                        >
                            ✕
                        </span>

                        <h3 style={{ color: "#fff", marginBottom: "14px" }}>
                            Confirm Booking
                        </h3>

                        {/* Vehicle Preview */}
                        <div style={{
                            display: "flex",
                            gap: "12px",
                            marginBottom: "16px"
                        }}>
                            <img
                                src={vehicle.coverImage}
                                alt={vehicle.carModel}
                                style={{
                                    width: "80px",
                                    height: "80px",
                                    borderRadius: "12px",
                                    objectFit: "cover"
                                }}
                            />

                            <div>
                                <div style={{ color: "#fff", fontWeight: "500" }}>
                                    {vehicle.carModel}
                                </div>

                                <div style={{ color: "#aaa", fontSize: "13px" }}>
                                    Driver: {vehicle.driverName}
                                </div>

                                <div style={{ color: "#b57cff", fontSize: "13px", marginTop: "4px" }}>
                                    ₹ {vehicle.pricePerKm} / km
                                </div>
                            </div>
                        </div>

                        {/* Booking Info */}
                        <div style={{
                            background: "#1f1f1f",
                            borderRadius: "12px",
                            padding: "12px",
                            marginBottom: "14px",
                            border: "1px solid #2a2a2a"
                        }}>
                            <div style={{ color: "#bbb", fontSize: "13px" }}>
                                📅 {fromDate} → {toDate}
                            </div>

                            <div style={{ color: "#aaa", fontSize: "13px", marginTop: "6px" }}>
                                Duration: {days} days
                            </div>
                        </div>

                        {/* Total */}
                        <div style={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            marginBottom: "18px"
                        }}>
                            <span style={{ color: "#aaa" }}>Total</span>

                            <span style={{
                                color: "#b57cff",
                                fontSize: "22px",
                                fontWeight: "600"
                            }}>
                                ₹{cost}
                            </span>
                        </div>

                        {/* Actions */}
                        <div style={{ display: "flex", gap: "10px" }}>
                            <button
                                onClick={() => setShowModal(false)}
                                style={{
                                    flex: 1,
                                    padding: "10px",
                                    background: "#2f2f2f",
                                    color: "#fff",
                                    borderRadius: "10px",
                                    border: "none",
                                    cursor: "pointer"
                                }}
                            >
                                Cancel
                            </button>

                            <button
                                onClick={confirmBooking}
                                disabled={bookingLoading}
                                style={{
                                    flex: 1,
                                    padding: "10px",
                                    background: "linear-gradient(135deg,#6d28d9,#9333ea)",
                                    color: "#fff",
                                    borderRadius: "10px",
                                    border: "none",
                                    cursor: "pointer"
                                }}
                            >
                                {bookingLoading ? "Booking..." : "Confirm"}
                            </button>
                        </div>

                    </div>
                </div>
            )}
        </div>
    );
};
export default VehicleDetails;
