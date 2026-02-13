import { useState } from 'react';
import { getAgencyById } from '../Services/agencyService';


export function useSearchAgencyById() {
    const [agency, setAgency] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const searchedAgency = async(agencyId) => {
        try{
            console.log("Searching for agency with ID:", agencyId);
            setLoading(true);
            const response = await getAgencyById(agencyId);
            setAgency(response.data);
            console.log("Fetched agency details:", response.data);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }
    return { agency, loading, error, searchedAgency };
}