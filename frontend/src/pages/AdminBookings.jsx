import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAdminBookings } from "../api/bookingApi";

export default function AdminBookings() {
	const [bookings, setBookings] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState("");

	useEffect(() => {
		getAdminBookings()
			.then(({ data }) => setBookings(Array.isArray(data) ? data : []))
			.catch((err) => setError(err.response?.data?.message || "Unable to load booking records."))
			.finally(() => setLoading(false));
	}, []);

	return (
		<section className="page-section">
			<div className="container">
				<span className="eyebrow">MANAGEMENT</span>
				<h1>Booking records</h1>
				<p className="lead"><Link className="text-link" to="/admin">← Admin dashboard</Link></p>
				{loading ? <p>Loading booking records...</p>
					: error ? <div className="error">{error}</div>
						: bookings.length === 0 ? <div className="empty"><h2>No bookings yet</h2></div>
							: (
								<div className="table-card">
									<table>
										<thead><tr><th>Booking</th><th>Package</th><th>Travel date</th><th>Guests</th><th>Amount</th><th>Status</th></tr></thead>
										<tbody>
											{bookings.map((booking) => (
												<tr key={booking.id}>
													<td>TG-{booking.id}</td>
													<td>{booking.travelPackage?.name || "—"}</td>
													<td>{booking.travelDate}</td>
													<td>{booking.guests}</td>
													<td>₹{Number(booking.totalAmount).toLocaleString("en-IN")}</td>
													<td>{booking.status}</td>
												</tr>
											))}
										</tbody>
									</table>
								</div>
							)}
			</div>
		</section>
	);
}
