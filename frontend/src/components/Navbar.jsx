import { Link, NavLink, useLocation, useNavigate } from "react-router-dom";
import { Menu, Plane, X } from "lucide-react";
import { useEffect, useState } from "react";

function getUser() {
	try {
		return JSON.parse(localStorage.getItem("travelgo_user") || "null");
	} catch {
		return null;
	}
}

export default function Navbar() {
	const [open, setOpen] = useState(false);
	const location = useLocation();
	const navigate = useNavigate();
	const user = getUser();
	const authenticated = Boolean(localStorage.getItem("travelgo_token"));

	useEffect(() => setOpen(false), [location.pathname]);

	const logout = () => {
		localStorage.removeItem("travelgo_token");
		localStorage.removeItem("travelgo_user");
		navigate("/");
	};

	return (
		<header className="navbar">
			<div className="nav-inner">
				<Link to="/" className="logo"><span className="logo-mark"><Plane size={19} /></span>TravelGo</Link>
				<button className="menu-btn" type="button" aria-label={open ? "Close navigation" : "Open navigation"} aria-expanded={open} onClick={() => setOpen(!open)}>
					{open ? <X /> : <Menu />}
				</button>
				<nav className={open ? "nav-links open" : "nav-links"} aria-label="Main navigation">
					<NavLink to="/">Home</NavLink>
					<NavLink to="/destinations">Destinations</NavLink>
					<NavLink to="/hotels">Hotels</NavLink>
					<NavLink to="/packages">Packages</NavLink>
					{authenticated && <NavLink to="/my-bookings">My Bookings</NavLink>}
					{authenticated && <NavLink to="/profile">Profile</NavLink>}
					{user?.role === "ADMIN" && <NavLink to="/admin">Admin</NavLink>}
					<Link to="/booking" className="btn btn-small">Create Plan</Link>
					{authenticated
						? <button className="nav-login" type="button" onClick={logout}>Log out</button>
						: <><Link to="/login" className="nav-login">Login</Link><Link to="/register" className="btn btn-small">Register</Link></>}
				</nav>
			</div>
		</header>
	);
}