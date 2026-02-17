import { useState } from "react";
import { cancelBooking } from "../Services/bookingService";

export function useCancelBooking() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const cancel = async (bookingId) => {
    try {
      setLoading(true);
      setError(null);

      await cancelBooking({
        bookingId: bookingId,   
      });

    } catch (err) {
      console.error("Error cancelling booking:", err);
      setError("Failed to cancel booking.");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { cancel, loading, error };
}
