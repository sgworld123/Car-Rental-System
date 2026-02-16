import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useVehicleById } from '../Hooks/useVehicleById';
import { useCreateBooking } from '../Hooks/useCreateBooking';

const VehicleDetails = () => {
    const location = useLocation();
    const { vehicleId } = useParams();
    const navigate = useNavigate();

    const [showModal, setShowModal] = useState(false);

    const[bookingLoading,setBookingLoading] = useState(false);

    const { vehicle, loading, error, fetchVehicle } = useVehicleById(vehicleId);

    const params = new URLSearchParams(location.search);
    const fromDate = params.get("from");
    const toDate = params.get("to");

    const { handleCreateBooking } = useCreateBooking();

    useEffect(() => {
        fetchVehicle();
    }, [vehicleId, fetchVehicle]);

    const handleBook = () => {
        if (!fromDate || !toDate) {
            alert("Search dates missing");
            return;
        }
        setShowModal(true);
    };

    const confirmBooking = async () => {
        const payload = {
            vehicleId,
            fromDate,
            toDate
        };
        try{
            setBookingLoading(true);
            await handleCreateBooking(payload);
            setBookingLoading(false);
            setShowModal(false);
            navigate("/dashboard/bookings");
        }
        catch(error){
            console.error("Booking failed:", error);
            alert("Failed to create booking. Please try again.");}
    }

    const days =
        fromDate && toDate
            ? Math.ceil((new Date(toDate) - new Date(fromDate)) / (1000 * 60 * 60 * 24))
            : 0;

    const totalPrice = vehicle && days > 0 ? vehicle.pricePerKm * days : 0;

    return (
        <div style={page}>
            {loading && <p style={{ color: "#aaa" }}>Loading...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

            {vehicle && (
                <div style={card}>
                    <h1 style={title}>{vehicle.model}</h1>

                    <div style={goldLine} />

                    <p style={text}>Make: {vehicle.make}</p>
                    <p style={text}>Year: {vehicle.year}</p>
                    <p style={price}>₹{vehicle.pricePerKm} / km</p>

                    <button style={bookBtn} onClick={handleBook}>
                        Book Now
                    </button>
                </div>
            )}

            {showModal && vehicle && (
                <div style={overlay}>
                    <div style={modal}>

                        {/* X Close */}
                        <span style={closeBtn} onClick={() => setShowModal(false)}>✕</span>

                        <h2 style={modalTitle}>Booking Summary</h2>

                        <div style={goldLine} />

                        <p style={modalText}>From: {fromDate}</p>
                        <p style={modalText}>To: {toDate}</p>
                        <p style={modalText}>Days: {days}</p>

                        <h3 style={total}>₹{totalPrice}</h3>

                        <div style={{ display: "flex", gap: 12, marginTop: 20 }}>
                            <button style={cancelBtn} onClick={() => setShowModal(false)}>
                                Cancel
                            </button>

                            <button style={confirmBtn} disabled={bookingLoading} onClick={confirmBooking}>
                                {bookingLoading ? "Booking..." : "Confirm"}
                            </button>
                        </div>

                    </div>
                </div>
            )}
        </div>
    );
};

const page = {
    minHeight: "100vh",
    background: "#0b0b0b",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
};

const card = {
    background: "#141414",
    padding: 40,
    borderRadius: 16,
    width: 400,
    boxShadow: "0 0 40px rgba(128,0,255,0.15)",
};

const title = {
    color: "#fff",
    marginBottom: 10,
};

const text = {
    color: "#aaa",
    marginTop: 8,
};

const price = {
    color: "#c084fc",
    fontSize: 20,
    marginTop: 15,
};

const bookBtn = {
    marginTop: 30,
    width: "100%",
    padding: 12,
    background: "#7c3aed",
    border: "none",
    borderRadius: 8,
    color: "#fff",
    cursor: "pointer",
    fontWeight: 600,
};

const goldLine = {
    height: 2,
    background: "linear-gradient(90deg, gold, transparent)",
    margin: "15px 0",
};

const overlay = {
    position: "fixed",
    inset: 0,
    background: "rgba(0,0,0,0.7)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
};

const modal = {
    background: "#111",
    color: "#fff",
    padding: 30,
    borderRadius: 14,
    width: 350,
    position: "relative",
    border: "1px solid #333",
};

const closeBtn = {
    position: "absolute",
    top: 12,
    right: 12,
    cursor: "pointer",
    color: "#aaa",
    fontSize: 18,
};

const modalTitle = {
    marginBottom: 10,
};

const modalText = {
    color: "#bbb",
    marginTop: 8,
};

const total = {
    marginTop: 15,
    color: "gold",
};

const cancelBtn = {
    flex: 1,
    padding: 10,
    background: "#222",
    border: "1px solid #444",
    color: "#fff",
    borderRadius: 6,
};

const confirmBtn = {
    flex: 1,
    padding: 10,
    background: "#7c3aed",
    border: "none",
    color: "#fff",
    borderRadius: 6,
};

export default VehicleDetails;
