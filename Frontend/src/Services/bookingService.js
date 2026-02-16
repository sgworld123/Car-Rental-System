import api from "./api";

export const createBooking = (bookingData) => {
  return api.post("/api/booking", bookingData);
};

export const cancelBooking = (bookingId) => {
  return api.post("/api/booking/cancel", { bookingId });
};

export const confirmBooking = (bookingId) => {
  return api.post("/api/booking/confirm", { bookingId });
};

export const getUserBookings = () => {
  return api.get(`/api/booking/my`);
};