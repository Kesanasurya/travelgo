import { Navigate, Outlet } from "react-router-dom";
export default function AdminRoute() { try { const user = JSON.parse(localStorage.getItem("travelgo_user") || "null"); return user?.role === "ADMIN" ? <Outlet /> : <Navigate to="/login" replace />; } catch { return <Navigate to="/login" replace />; } }
