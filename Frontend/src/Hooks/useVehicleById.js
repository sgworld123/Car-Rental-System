import { useState } from "react";
import { getVehicleById } from "../Services/agencyService";

export function useVehicleById(vehicleId) {
    const [vehicle, setVehicle] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const fetchVehicle = async () => {
        try {
            const response = await getVehicleById(vehicleId);
            setVehicle(response.data);
        }
        catch (err) {
            setError("Failed to load vehicle details");
        }
        finally {
            setLoading(false);
        }
    };
    return { vehicle, loading, error, fetchVehicle };
}