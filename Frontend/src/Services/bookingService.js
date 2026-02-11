import axios from "axios";

const API_URL = "http://localhost:8090/api/booking";

export const createBooking = (bookingData) => {
  return axios.post(`${API_URL}/create`, bookingData);
};