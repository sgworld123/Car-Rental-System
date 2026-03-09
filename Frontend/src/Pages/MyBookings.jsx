import React, { useEffect, useState } from "react";
import { getUserBookings } from "../Services/bookingService";
import { useCancelBooking } from "../Hooks/useCancelBooking";

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [cancellingIds, setCancellingIds] = useState(new Set());
  const pollRefs = useRef({});

  const { cancel, cancelError } = useCancelBooking();

  const fetchBookings = async () => {
    try {
      const response = await getUserBookings();
      const data = Array.isArray(response.data)
        ? response.data
        : response.data
        ? [response.data]
        : [];
      setBookings(data);
      return data;
    } catch (err) {
      console.error("Error fetching bookings:", err);
      setError("Failed to load bookings.");
      return [];
    }
  };

  useEffect(() => {
    fetchBookings().finally(() => setLoading(false));

    // Cleanup all polls on unmount
    return () => {
      Object.values(pollRefs.current).forEach(({ interval, timeout }) => {
        clearInterval(interval);
        clearTimeout(timeout);
      });
    };
  }, []);

  const handleCancel = async (id) => {
    try {
      await cancel(id);

      // Mark as cancelling (intermediate state)
      setCancellingIds((prev) => new Set(prev).add(id));
      setBookings((prev) =>
        prev.map((b) => (b.bookingId === id ? { ...b, status: "CANCELLING" } : b))
      );

      // Poll every 2s for up to 30s
      const interval = setInterval(async () => {
        const data = await fetchBookings();
        const updated = data.find((b) => b.bookingId === id);
        if (updated?.status === "CANCELLED") {
          clearInterval(interval);
          clearTimeout(pollRefs.current[id]?.timeout);
          delete pollRefs.current[id];
          setCancellingIds((prev) => {
            const next = new Set(prev);
            next.delete(id);
            return next;
          });
        }
      }, 2000);

      // Stop polling after 30s
      const timeout = setTimeout(() => {
        clearInterval(interval);
        delete pollRefs.current[id];
        setCancellingIds((prev) => {
          const next = new Set(prev);
          next.delete(id);
          return next;
        });
      }, 30000);

      pollRefs.current[id] = { interval, timeout };
    } catch (e) {
      console.error("Cancel failed", e);
      setCancellingIds((prev) => {
        const next = new Set(prev);
        next.delete(id);
        return next;
      });
    }
  };

  return (
    <div style={page}>
      <h1 style={title}>My Bookings</h1>

      {loading && <p style={infoText}>Loading bookings...</p>}
      {error && <p style={errorText}>{error}</p>}
      {cancelError && <p style={errorText}>Cancel failed.</p>}

      {!loading && bookings.length === 0 && (
        <p style={infoText}>No bookings yet.</p>
      )}

      <div style={list}>
        {bookings.map((b) => {
          const isCancelling = cancellingIds.has(b.bookingId);

          return (
            <div key={b.bookingId} style={card}>
              <div style={row}>
                <h3 style={vehicle}>Vehicle ID: {b.vehicleId}</h3>
                <span
                  style={{
                    ...statusBadge,
                    background:
                      b.status === "CONFIRMED"
                        ? "#14532d"
                        : b.status === "CANCELLED"
                        ? "#7f1d1d"
                        : b.status === "CANCELLING"
                        ? "#92400e"
                        : "#3f3f46",
                  }}
                >
                  {b.status}
                </span>
              </div>

              <div style={divider} />

              <p style={text}>From: {b.fromDate}</p>
              <p style={text}>To: {b.endDate}</p>
              <h3 style={price}>₹{b.cost}</h3>

              {(b.status === "PENDING" || isCancelling) && (
                <button
                  onClick={() => handleCancel(b.bookingId)}
                  disabled={isCancelling}
                  style={{
                    marginTop: 12,
                    padding: "8px 16px",
                    background: isCancelling ? "#444" : "#b91c1c",
                    border: "none",
                    borderRadius: 6,
                    color: "#fff",
                    cursor: isCancelling ? "not-allowed" : "pointer",
                  }}
                >
                  {isCancelling ? "Cancelling..." : "Cancel Booking"}
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

/* ---------- STYLES ---------- */

const page = {
  minHeight: "100vh",
  background: "#0b0b0b",
  padding: 40,
};

const title = {
  color: "#fff",
  marginBottom: 30,
};

const list = {
  display: "flex",
  flexDirection: "column",
  gap: 20,
  maxWidth: 800,
};

const card = {
  background: "#141414",
  padding: 24,
  borderRadius: 16,
  border: "1px solid #222",
  boxShadow: "0 0 20px rgba(124,58,237,0.15)",
};

const row = {
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
};

const vehicle = {
  color: "#fff",
};

const text = {
  color: "#aaa",
  marginTop: 8,
};

const price = {
  color: "gold",
  marginTop: 15,
};

const statusBadge = {
  padding: "4px 12px",
  borderRadius: 20,
  fontSize: 12,
  color: "#fff",
};

const divider = {
  height: 1,
  background: "#222",
  margin: "12px 0",
};

const infoText = {
  color: "#777",
};

const errorText = {
  color: "red",
};

export default MyBookings;
