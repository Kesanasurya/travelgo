import api from "./axios";
export const getPackages = () => api.get("/packages");
