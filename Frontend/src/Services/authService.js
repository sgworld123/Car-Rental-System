import axios from 'axios';

const API_URL = 'http://localhost:8090/api/auth';

export const login = (credentials) => {
    return axios.post(`${API_URL}/login`,credentials);
}

export const register = (userData) => {
    return axios.post(`${API_URL}/signup`,userData);
}