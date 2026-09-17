import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getHotels } from "../api/hotelApi";
export default function HotelDetails() { const { id } = useParams(); const [hotel, setHotel] = useState(null); useEffect(() => { getHotels().then(({ data }) => setHotel(data.find(item => String(item.id) === id))).catch(() => {}); }, [id]); return <section className="page-section"><div className="container narrow">{hotel ? <><span className="eyebrow">HOTEL</span><h1>{hotel.name}</h1><p>{hotel.description}</p><strong>₹{Number(hotel.pricePerNight).toLocaleString("en-IN")} per night</strong></> : <p>Loading hotel...</p>}<Link className="btn" to="/hotels">Back to hotels</Link></div></section>; }
