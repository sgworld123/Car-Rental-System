import { getUserBookings } from "../Services/bookingService"

export function useGetBookingsById() {
    const getBookingsById = async () => {
        try {
            const response = await getUserBookings();
            return response.data;
        } catch (error) {
            throw error;
        }
    }
    return { getBookingsById }
}