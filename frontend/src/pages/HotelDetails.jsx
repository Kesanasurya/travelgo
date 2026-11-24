import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import api from "../api/axios";
import { hotels as fallbackHotels } from "../data";

// The API returns { destination: {name}, pricePerNight, imageUrl }; the built-in list uses plain fields.
const normalize = (h) => ({
	...h,
	destination: typeof h.destination === "string" ? h.destination : h.destination?.name || "",
	price: h.price ?? h.pricePerNight,
	image: h.image || h.imageUrl,
});

export default function HotelDetails() {
	const { id } = useParams();
	const [hotel, setHotel] = useState(undefined);

	useEffect(() => {
		setHotel(undefined);
		api.get(`/hotels/${encodeURIComponent(id)}`)
			.then(({ data }) => setHotel(normalize(data)))
			.catch(() => {
				const local = fallbackHotels.find((item) => String(item.id) === id);
				setHotel(local ? normalize(local) : null);
			});
	}, [id]);

	if (hotel === undefined) {
		return (
			<section className="page-section">
				<div className="container narrow"><p>Loading stay…</p></div>
			</section>
		);
	}

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
				{hotel.image && <img className="hotel-detail-image" src={hotel.image} alt={hotel.name} />}
				<p className="lead">{hotel.description || `A comfortable place to make the most of your trip to ${hotel.destination}.`}</p>
				{hotel.location && <p>📍 {hotel.location}</p>}
				<p><span className="rating">★ {hotel.rating}</span></p>
				{hotel.amenities && <p>{hotel.amenities}</p>}
				<strong>₹{Number(hotel.price).toLocaleString("en-IN")} per night</strong>
				<p className="demo-payment-note">Hotel booking is arranged through a matching destination trip package.</p>
				<Link className="btn" to={`/booking?destination=${encodeURIComponent(hotel.destination)}`}>Explore a trip to {hotel.destination}</Link>
				<p><Link className="text-link" to="/hotels">Back to all stays →</Link></p>
			</div>
		</section>
	);
}
