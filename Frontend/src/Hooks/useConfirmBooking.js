import { confirmBooking } from "../Services/bookingService";

export function useConfirmBooking() {
    const handleConfirmBooking = async (bookingId) => {
        try {
            const response = await confirmBooking(bookingId);
            console.log("Booking confirmed:", response.data);
            return response.data;
        } catch (error) {
            console.error("Error confirming booking:", error.response ? error.response.data : error.message);
            console.error("Error confirming booking:", error);
            throw error;
        }
    };

    return { handleConfirmBooking };
}