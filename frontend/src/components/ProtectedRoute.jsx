import { Navigate, Outlet, useLocation } from "react-router-dom";

export default function ProtectedRoute() {
	const location = useLocation();
	return localStorage.getItem("travelgo_token")
		? <Outlet />
		: <Navigate to="/login" state={{ from: location }} replace />;
}
