import api from "./axios";
export const getHotels = () => api.get("/hotels");
