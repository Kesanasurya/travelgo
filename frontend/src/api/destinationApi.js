import api from "./axios";
export const getDestinations = () => api.get("/destinations");
export const getDestination = (id) => api.get(`/destinations/${id}`);
