import { useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { CreditCard, Landmark, Smartphone } from "lucide-react";
import { createBooking } from "../api/bookingApi";

function getBookingDraft() {
	try {
		return JSON.parse(sessionStorage.getItem("travelgo_booking_draft") || "null");
	} catch {
		return null;
	}
}

const paymentMethods = [
	{ id: "upi", label: "UPI", description: "Use a UPI app (demo only)", Icon: Smartphone },
	{ id: "card", label: "Credit or debit card", description: "Card checkout preview (demo only)", Icon: CreditCard },
	{ id: "netbanking", label: "Net banking", description: "Bank checkout preview (demo only)", Icon: Landmark }
];

export default function Payment() {
	const navigate = useNavigate();
	const bookingDraft = getBookingDraft();
	const [method, setMethod] = useState("upi");
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);

	if (!bookingDraft?.packageId || !bookingDraft.travelDate || !Number.isFinite(Number(bookingDraft.estimatedAmount))) {
		return <Navigate to="/booking" replace />;
	}

	const completeDemoPayment = async (event) => {
		event.preventDefault();
		setLoading(true);
		setError("");
		try {
			const { data: booking } = await createBooking({
				packageId: Number(bookingDraft.packageId),
				roomType: bookingDraft.roomType,
				travelDate: bookingDraft.travelDate,
				guests: Number(bookingDraft.guests)
			});
			const payment = {
				bookingId: booking.id,
				amount: Number(booking.totalAmount),
				method,
				reference: `TG-DEMO-${Date.now()}`,
				paidAt: new Date().toISOString()
			};
			sessionStorage.setItem("travelgo_booking", JSON.stringify(booking));
			sessionStorage.setItem("travelgo_payment", JSON.stringify(payment));
			navigate("/payment/confirmation", { state: { booking, payment } });
		} catch (err) {
			setError(err.response?.data?.message || "Unable to confirm this booking. Please check your details and try again.");
		} finally {
			setLoading(false);
		}
	};

	return (
		<section className="page-section payment-page">
			<div className="container payment-container">
				<div className="payment-heading">
					<span className="eyebrow">CHECKOUT</span>
					<h1>Payment</h1>
					<p className="lead">Review your trip and choose a demo payment method.</p>
				</div>
				<div className="demo-payment-banner" role="note">
					<strong>Demo checkout — no real payment is processed.</strong>
					<span>Do not enter card numbers, bank details, UPI IDs or passwords. Connect a payment provider before accepting real payments.</span>
				</div>
				<div className="payment-layout">
					<form className="form-card payment-methods" onSubmit={completeDemoPayment}>
						<h2>Choose a method</h2>
						{error && <div className="error" role="alert">{error}</div>}
						<div className="payment-method-list">
							{paymentMethods.map(({ id, label, description, Icon }) => (
								<label className={`payment-method${method === id ? " selected" : ""}`} key={id}>
									<input
										type="radio"
										name="paymentMethod"
										value={id}
										checked={method === id}
										onChange={() => setMethod(id)}
									/>
									<Icon size={21} aria-hidden="true" />
									<span><strong>{label}</strong><small>{description}</small></span>
								</label>
							))}
						</div>
						<button className="btn full" type="submit" disabled={loading}>
							{loading ? "Confirming booking..." : `Complete demo checkout · ₹${Number(bookingDraft.estimatedAmount).toLocaleString("en-IN")}`}
						</button>
						<Link className="payment-back-link" to="/booking">Review trip details</Link>
					</form>
					<aside className="payment-summary">
						<span className="eyebrow">TRIP SUMMARY</span>
						<h2>{bookingDraft.packageName || "Your travel package"}</h2>
						<p>{bookingDraft.destination || "Travel package"} · {bookingDraft.roomType}</p>
						<div className="payment-summary-row"><span>Travel date</span><strong>{bookingDraft.travelDate}</strong></div>
						<div className="payment-summary-row"><span>Guests</span><strong>{bookingDraft.guests}</strong></div>
						<div className="payment-summary-total"><span>Estimated total</span><strong>₹{Number(bookingDraft.estimatedAmount).toLocaleString("en-IN")}</strong></div>
						<p className="payment-summary-note">The server confirms the final booking amount. This demo does not charge you or store payment credentials.</p>
					</aside>
				</div>
			</div>
		</section>
	);
}
