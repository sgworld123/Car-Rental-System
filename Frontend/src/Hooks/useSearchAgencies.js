import React from "react";
import { searchAgencies } from "../Services/agencyService";

export function useSearchAgencies() {
    const[searchResults, setSearchResults] = React.useState([]);

    const searchAgency = async (searchData) => {
        try {
            const response = await searchAgencies(searchData)
            setSearchResults(response.data.agencies);
        } catch (error) {
            console.error("Error searching agencies:", error);
        }
    };
    return { searchResults, searchAgency };
}