import { createBooking } from "../Services/bookingService";

export function useCreateBooking() {

    const handleCreateBooking = async (payload) => {
        try {
            const response = await createBooking(payload);
            console.log("Booking creation response:", response);
            console.log("Booking created successfully:", response.data);
            return response.data;   
        } catch (error) {
            console.error("Error response:", error.response?.data); 
            console.error("Error creating booking:", error);
            throw error;  
        }
    };

    return { handleCreateBooking };
}
