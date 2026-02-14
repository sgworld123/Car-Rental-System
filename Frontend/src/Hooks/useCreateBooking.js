import { useState } from "react";
import { createBooking } from "../Services/bookingService";

export function useCreateBooking() {
    const[bookingData,setBookingData] = useState(null);

    const handleCreateBooking = async() => {
        try {
            const response = await createBooking(bookingData);
            setBookingData(response.data);
            console.log("Booking created successfully:", response.data);
        } catch (error) {
            console.error("Error creating booking:", error);
        }
    }
    return { bookingData, handleCreateBooking };
}
