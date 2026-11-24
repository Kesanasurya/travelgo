import { Link } from "react-router-dom";

export default function PackageCard({ p }) {
	const destination = typeof p.destination === "string" ? p.destination : p.destination?.name;
	return (
		<article className="card package-card">
			<img src={p.image || p.imageUrl} alt={p.name} />
			<div className="card-body">
				<span className="eyebrow">{destination}</span>
				<h3><Link to={`/packages/${p.id}`}>{p.name}</Link></h3>
				<p>{p.duration}</p>
				<div className="card-row">
					<strong>₹{Number(p.price).toLocaleString("en-IN")}</strong>
					<span>
						<Link className="text-link" to={`/packages/${p.id}`}>Details</Link>{" "}
						<Link className="btn btn-small" to={`/booking?packageId=${encodeURIComponent(p.id)}`}>Book Now</Link>
					</span>
				</div>
			</div>
		</article>
	);
}