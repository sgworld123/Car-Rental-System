import api from "./api";

export const createBooking = (bookingData) => {
  return api.post("/api/booking/create", bookingData);
};

export const cancelBooking = (bookingId) => {
  return api.post("/api/booking/cancel", { bookingId });
};

export const confirmBooking = (bookingId) => {
  return api.post("/api/booking/confirm", { bookingId });
};

export const getUserBookings = (userId) => {
  return api.get(`/api/booking/user/${userId}`);
};