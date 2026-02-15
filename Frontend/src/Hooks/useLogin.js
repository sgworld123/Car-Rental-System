import { useState } from 'react';
import { login as loginFun } from '../Services/authService';

export function useLogin() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const login = async (username, password) => {
        setLoading(true);
        setError(null)
        try {
            console.log("SENDING LOGIN:", { username, password }); // 👈 ADD

            const response = await loginFun({ username, password });

            console.log("LOGIN RESPONSE:", response.data); // 👈 ADD

            localStorage.setItem("token", response.data.jwt);
            // console.log('Login ongoing', { username, password });
            // const response = await loginFun({ username, password });
            // localStorage.setItem('token', response.data.token);
            return true;
        }
        catch (err) {
            setError(err.response?.data?.message || 'Login failed');
            return false;
        }
        finally {
            setLoading(false);
        }
    }
    return { login, loading, error };
}