import { useState } from "react";
import { userInfo } from "../Services/authService";

export function useGetUserInfo() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const getInfo = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await userInfo();
            return response;

        } catch (e) {
            setError(e);
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    return { loading, error, getInfo };
}
