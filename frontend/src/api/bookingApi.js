import api from "./axios";
export const createBooking = (payload) => api.post("/bookings", payload);
export const getMyBookings = () => api.get("/bookings/my");
export const cancelBooking = (id) => api.put(`/bookings/${id}/cancel`);
