import { useEffect, useState } from "react";
import { getHotels } from "../api/hotelApi";
import { hotels as fallbackHotels } from "../data";
import HotelCard from "../components/HotelCard";

export default function Hotels() {
	const [hotels, setHotels] = useState(null);

	useEffect(() => {
		getHotels()
			.then(({ data }) => setHotels(Array.isArray(data) && data.length ? data : fallbackHotels))
			// The free backend can be asleep or unreachable; show the built-in list instead of an empty page.
			.catch(() => setHotels(fallbackHotels));
	}, []);

	return (
		<section className="page-section">
			<div className="container">
				<span className="eyebrow">HANDPICKED STAYS</span>
				<h1>Hotels</h1>
				<p className="lead">Comfortable stays for every kind of traveler.</p>
				{hotels === null
					? <p>Loading stays… the first visit can take up to a minute while the server wakes up.</p>
					: <div className="grid-4">{hotels.map((h) => <HotelCard h={h} key={h.id} />)}</div>}
			</div>
		</section>
	);
}
