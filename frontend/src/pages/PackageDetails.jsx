import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getPackages } from "../api/packageApi";

export default function PackageDetails() {
	const { id } = useParams();
	const [item, setItem] = useState(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState("");

	useEffect(() => {
		let active = true;
		setLoading(true);
		setError("");
		getPackages()
			.then(({ data }) => {
				if (active) setItem(data.find((value) => String(value.id) === id) || null);
			})
			.catch((err) => {
				if (active) setError(err.response?.data?.message || "Unable to load this travel package.");
			})
			.finally(() => {
				if (active) setLoading(false);
			});
		return () => { active = false; };
	}, [id]);

	if (loading) return <section className="page-section"><div className="container narrow"><p>Loading package...</p></div></section>;
	if (error || !item) {
		return (
			<section className="page-section">
				<div className="container narrow">
					<span className="eyebrow">TRAVEL PACKAGE</span>
					<h1>Package unavailable</h1>
					<p className="lead">{error || "We couldn’t find that travel package."}</p>
					<Link className="btn" to="/packages">Browse packages</Link>
				</div>
			</section>
		);
	}

	const destination = typeof item.destination === "string" ? item.destination : item.destination?.name;
	return (
		<>
			{(item.imageUrl || item.image) && (
				<section className="detail-hero" style={{ backgroundImage: `url(${item.imageUrl || item.image})` }}>
					<div><span className="pill">{destination}</span><h1>{item.name}</h1></div>
				</section>
			)}
			<section className="page-section">
				<div className="container narrow">
					<span className="eyebrow">TRAVEL PACKAGE · {destination}</span>
					<h1>{item.name}</h1>
					<p className="lead">{item.description || "A thoughtfully planned trip, ready when you are."}</p>
					<p>{item.duration}</p>
					<h2>₹{Number(item.price).toLocaleString("en-IN")} <small>per guest</small></h2>
					<p className="demo-payment-note">Secure your trip, then complete the clearly marked demo checkout. No real payment is processed.</p>
					<Link className="btn" to={`/booking?packageId=${encodeURIComponent(item.id)}`}>Book this package</Link>
				</div>
			</section>
		</>
	);
}
