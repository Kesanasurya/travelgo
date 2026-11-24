import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { getPackages } from "../api/packageApi";

function getBookingDraft() {
	try {
		const draft = JSON.parse(sessionStorage.getItem("travelgo_booking_draft") || "null");
		return draft ? {
			packageId: String(draft.packageId || ""),
			roomType: draft.roomType || "Deluxe Room",
			travelDate: draft.travelDate || "",
			guests: String(draft.guests || "2")
		} : null;
	} catch {
		return null;
	}
}

export default function Booking() {
	const nav = useNavigate();
	const [searchParams] = useSearchParams();
	const [form, setForm] = useState(() => getBookingDraft() || { packageId: "", roomType: "Deluxe Room", travelDate: "", guests: "2" });
	const [packages, setPackages] = useState([]);
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
	const [packagesLoading, setPackagesLoading] = useState(true);
	const selectedPackage = packages.find((travelPackage) => String(travelPackage.id) === form.packageId);

	useEffect(() => {
		getPackages()
			.then(({ data }) => {
				const availablePackages = Array.isArray(data) ? data : [];
				setPackages(availablePackages);
				if (availablePackages.length > 0) {
					const requestedPackage = searchParams.get("packageId") || getBookingDraft()?.packageId;
					const requestedDestination = searchParams.get("destination")?.toLowerCase();
					const matchingPackage = availablePackages.find((travelPackage) => {
						const destination = typeof travelPackage.destination === "string"
							? travelPackage.destination
							: travelPackage.destination?.name;
						return (requestedPackage && String(travelPackage.id) === requestedPackage)
							|| (requestedDestination && destination?.toLowerCase() === requestedDestination);
					});
					setForm((current) => ({
						...current,
						packageId: String(matchingPackage?.id ?? availablePackages[0].id)
					}));
				} else {
					setError("No travel packages are currently available.");
				}
			})
			.catch((err) => {
				setError(err.response?.data?.message || "Unable to load travel packages.");
			})
			.finally(() => setPackagesLoading(false));
	}, [searchParams]);

	const change = (event) => setForm({ ...form, [event.target.name]: event.target.value });

	const submit = async (event) => {
		event.preventDefault();
		setLoading(true);
		setError("");
		try {
			sessionStorage.setItem("travelgo_booking_draft", JSON.stringify({
				...form,
				packageId: Number(form.packageId),
				guests: Number(form.guests),
				packageName: selectedPackage?.name,
				destination: typeof selectedPackage?.destination === "string"
					? selectedPackage.destination
					: selectedPackage?.destination?.name,
				estimatedAmount: Number(selectedPackage?.price) * Number(form.guests)
			}));
			sessionStorage.removeItem("travelgo_booking");
			sessionStorage.removeItem("travelgo_payment");
			nav("/payment");
		} catch {
			setError("Unable to save your trip details. Please try again.");
		} finally {
			setLoading(false);
		}
	};

	return (
		<section className="page-section">
			<div className="container narrow">
				<span className="eyebrow">BOOK YOUR TRIP</span>
				<h1>Booking</h1>
				<form className="form-card" onSubmit={submit}>
					{error && <div className="error">{error}</div>}
					<label>
						Travel package
						<select name="packageId" value={form.packageId} onChange={change} required>
							{packagesLoading && <option value="">Loading packages...</option>}
							{!packagesLoading && packages.length === 0 && <option value="">No packages available</option>}
							{packages.map((travelPackage) => (
								<option key={travelPackage.id} value={travelPackage.id}>
									{travelPackage.name}
								</option>
							))}
						</select>
					</label>
					<label>
						Room type
						<select name="roomType" value={form.roomType} onChange={change}>
							<option>Standard Room</option>
							<option>Deluxe Room</option>
							<option>Suite</option>
							<option>Family Room</option>
						</select>
					</label>
					<label>
						Travel date
						<input
							required
							min={new Date().toISOString().slice(0, 10)}
							type="date"
							name="travelDate"
							value={form.travelDate}
							onChange={change}
						/>
					</label>
					<label>
						Guests
						<select name="guests" value={form.guests} onChange={change}>
							<option>1</option>
							<option>2</option>
							<option>3</option>
							<option>4</option>
						</select>
					</label>
					{selectedPackage && (
						<div className="booking-estimate">
							<span>Estimated trip total</span>
							<strong>₹{(Number(selectedPackage.price) * Number(form.guests)).toLocaleString("en-IN")}</strong>
							<small>Calculated from package price and guest count; confirmed by the server.</small>
						</div>
					)}
					<p className="demo-payment-note">Next: demo checkout. No real payment details or card information are collected.</p>
					<button className="btn full" disabled={loading || packagesLoading || packages.length === 0}>
						{loading ? "Creating booking..." : "Continue to payment"}
					</button>
				</form>
			</div>
		</section>
	);
}