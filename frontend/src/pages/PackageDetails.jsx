import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getPackages } from "../api/packageApi";
export default function PackageDetails() { const { id } = useParams(); const [item, setItem] = useState(null); useEffect(() => { getPackages().then(({ data }) => setItem(data.find(value => String(value.id) === id))).catch(() => {}); }, [id]); return <section className="page-section"><div className="container narrow">{item ? <><span className="eyebrow">TRAVEL PACKAGE</span><h1>{item.name}</h1><p>{item.description}</p><p>{item.duration}</p><strong>₹{Number(item.price).toLocaleString("en-IN")}</strong><br /><Link className="btn" to="/booking">Book this package</Link></> : <p>Loading package...</p>}</div></section>; }
