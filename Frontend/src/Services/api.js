import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8090"
});

// Attach access token to every request
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    const isAuthRoute = config.url.includes("/api/auth");
    if (token && !isAuthRoute) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const original = error.config;

        if (error.response?.status === 401 && !original._retry) {
            original._retry = true;

            try {
                const refreshToken = localStorage.getItem("refreshToken");

                console.log("REFRESH TOKEN FROM STORAGE:", refreshToken); // is it null?

                const { data } = await axios.post(
                    "http://localhost:8090/api/auth/refresh",
                    { refreshToken }
                );

                console.log("REFRESH RESPONSE:", data); // does it have data.jwt?

                localStorage.setItem("token", data.jwt);
                delete original.headers.Authorization;
                return api(original);

            } catch (err) {
                // Refresh token expired → force logout
                localStorage.removeItem("token");
                localStorage.removeItem("refreshToken");
                window.location.href = "/";
            }
        }

        return Promise.reject(error);
    }
);

export default api;