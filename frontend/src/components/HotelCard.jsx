import { Link } from "react-router-dom";

export default function HotelCard({ h }) {
	const destination = typeof h.destination === "string" ? h.destination : h.destination?.name;
	const price = h.price ?? h.pricePerNight;
	return (
		<article className="card hotel-card">
			<img src={h.image || h.imageUrl} alt={h.name} />
			<div className="card-body">
				<div className="card-row">
					<h3><Link to={`/hotels/${h.id}`}>{h.name}</Link></h3>
					<span className="rating">★ {h.rating}</span>
				</div>
				<p>{destination}</p>
				<div className="card-row">
					<strong>₹{Number(price).toLocaleString("en-IN")} <small>/ night</small></strong>
					<span>
						<Link className="text-link" to={`/hotels/${h.id}`}>Details</Link>{" "}
						<Link className="btn btn-small" to={`/booking?destination=${encodeURIComponent(destination || "")}`}>Book trip</Link>
					</span>
				</div>
			</div>
		</article>
	);
}