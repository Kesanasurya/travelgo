import { Link, Navigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { Check } from "lucide-react";

function readSessionValue(key) {
	try {
		return JSON.parse(sessionStorage.getItem(key) || "null");
	} catch {
		return null;
	}
}

export default function PaymentConfirmation() {
	const location = useLocation();
	const [session, setSession] = useState({
		booking: location.state?.booking || null,
		payment: location.state?.payment || null
	});
	const [ready, setReady] = useState(false);

	useEffect(() => {
		const storedBooking = readSessionValue("travelgo_booking");
		const storedPayment = readSessionValue("travelgo_payment");
		setSession((current) => ({
			booking: current.booking || storedBooking,
			payment: current.payment || storedPayment
		}));
		if (storedBooking?.id && storedPayment?.reference) {
			sessionStorage.removeItem("travelgo_booking_draft");
		}
		setReady(true);
	}, [location.key]);

	if (!ready) return <section className="page-section"><div className="container narrow"><p>Preparing your confirmation...</p></div></section>;

	const { booking, payment } = session;

	if (!booking?.id) return <Navigate to="/booking" replace />;
	if (!payment?.reference || String(payment.bookingId) !== String(booking.id)) {
		return <Navigate to="/payment" replace />;
	}

	return (
		<section className="page-section">
			<div className="container narrow">
				<div className="payment-confirmation success">
					<div className="payment-success-icon"><Check size={30} /></div>
					<span className="eyebrow">BOOKING TG-{booking.id}</span>
					<h1>Trip booked. Now look forward to it.</h1>
					<p>Your booking is confirmed. The payment step was a demo only — no money was charged.</p>
					<div className="payment-receipt">
						<div><span>Trip</span><strong>{booking.travelPackage?.name || "Travel package"}</strong></div>
						<div><span>Travel date</span><strong>{booking.travelDate}</strong></div>
						<div><span>Guests</span><strong>{booking.guests}</strong></div>
						<div><span>Demo checkout</span><strong>₹{Number(payment.amount).toLocaleString("en-IN")}</strong></div>
						<div><span>Reference</span><strong>{payment.reference}</strong></div>
					</div>
					<div className="payment-confirmation-actions">
						<Link className="btn" to="/my-bookings">View my bookings</Link>
						<Link className="text-link" to="/destinations">Keep exploring →</Link>
					</div>
				</div>
			</div>
		</section>
	);
}
