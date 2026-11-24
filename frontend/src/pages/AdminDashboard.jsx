import { Link } from "react-router-dom";

const sections = [
	["Destinations", "/admin/destinations"],
	["Hotels", "/admin/hotels"],
	["Packages", "/admin/packages"],
	["Bookings", "/admin/bookings"]
];

export default function AdminDashboard() {
	return (
		<section className="page-section">
			<div className="container">
				<span className="eyebrow">MANAGEMENT</span>
				<h1>Admin Dashboard</h1>
				<p className="lead">Choose a section to manage and review TravelGo.</p>
				<nav className="admin-shortcuts" aria-label="Admin sections">
					{sections.map(([label, path]) => <Link className="btn btn-small" to={path} key={path}>{label}</Link>)}
				</nav>
				<div className="table-card">
					<h2>Admin workspace</h2>
					<p className="lead">Manage destination, hotel, and package listings, or view booking records.</p>
					<Link className="text-link" to="/admin/bookings">Open booking records →</Link>
				</div>
			</div>
		</section>
	);
}