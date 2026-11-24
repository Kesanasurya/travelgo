import axios from "axios";

const configuredBaseUrl = import.meta.env.VITE_API_URL?.trim();
const baseURL = (configuredBaseUrl || "http://localhost:5000/api").replace(/\/+$/, "");

// 90s: a sleeping free-tier backend (Render) can take about a minute to wake up.
const api = axios.create({ baseURL, timeout: 90000 });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("travelgo_token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !window.location.pathname.includes("/login")) {
      localStorage.removeItem("travelgo_token");
      localStorage.removeItem("travelgo_user");
    }
    return Promise.reject(error);
  }
);

export default api;
