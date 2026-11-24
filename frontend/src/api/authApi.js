import api from "./axios";

export const register = (payload) => api.post("/auth/register", payload);
export const login = (payload) => api.post("/auth/login", payload);
export const getMe = () => api.get("/auth/me");
export const logout = () => {
  localStorage.removeItem("travelgo_token");
  localStorage.removeItem("travelgo_user");
};
