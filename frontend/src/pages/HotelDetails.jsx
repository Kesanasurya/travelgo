import { Link, useParams } from "react-router-dom";
import { hotels } from "../data";

export default function HotelDetails() {
	const { id } = useParams();
	const hotel = hotels.find((item) => String(item.id) === id);

	if (!hotel) {
		return (
			<section className="page-section">
				<div className="container narrow">
					<span className="eyebrow">HOTEL</span>
					<h1>Stay unavailable</h1>
					<p className="lead">We couldn’t find that hotel.</p>
					<Link className="btn" to="/hotels">Browse stays</Link>
				</div>
			</section>
		);
	}

	return (
		<section className="page-section">
			<div className="container narrow">
				<span className="eyebrow">STAY IN {hotel.destination.toUpperCase()}</span>
				<h1>{hotel.name}</h1>
				<img className="hotel-detail-image" src={hotel.image} alt={hotel.name} />
				<p className="lead">A comfortable place to make the most of your trip to {hotel.destination}.</p>
				<p><span className="rating">★ {hotel.rating}</span></p>
				<strong>₹{Number(hotel.price).toLocaleString("en-IN")} per night</strong>
				<p className="demo-payment-note">Hotel booking is arranged through a matching destination trip package.</p>
				<Link className="btn" to={`/booking?destination=${encodeURIComponent(hotel.destination)}`}>Explore a trip to {hotel.destination}</Link>
				<p><Link className="text-link" to="/hotels">Back to all stays →</Link></p>
			</div>
		</section>
	);
}
