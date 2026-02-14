import { useEffect } from "react"
import { useCreateBooking } from "../Hooks/useCreateBooking"

export function ConfirmBooking() {
    const { bookingData, handleCreateBooking } = useCreateBooking();
    useEffect(() => {
        console.log("ConfirmBooking component mounted");
        const fetchedData = async () => {
            await handleCreateBooking();
        }
        fetchedData();
    }, [])
    return (
        <h1>Confirm Bookine</h1>
    )
}

