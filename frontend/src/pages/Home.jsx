import { useState } from "react";
import { ArrowUpRight, Headphones, MapPin, Search, ShieldCheck, Wallet } from "lucide-react";
import { Link } from "react-router-dom";
import { destinations, hotels, packages } from "../data";
import DestinationCard from "../components/DestinationCard";
import HotelCard from "../components/HotelCard";
import PackageCard from "../components/PackageCard";
import "../Home.css";

export default function Home() {
  const [q, setQ] = useState("");
  const filtered = destinations.filter((destination) =>
    destination.name.toLowerCase().includes(q.toLowerCase())
  );
  const bali = destinations.find((destination) => destination.name === "Bali");
  const goa = destinations.find((destination) => destination.name === "Goa");

  return (
    <>
      <section className="hero">
        <div className="hero-overlay" />
        <div className="container hero-content">
          <span className="pill">✦ GOOD TRIPS START WITH A PLACE</span>
          <h1>
            Find your kind<br />
            of <em>somewhere.</em>
          </h1>
          <p>
            A quiet beach, a new city, a weekend away — find the place, stay
            and trip that fit the way you want to travel.
          </p>
          <div className="search-box">
            <div>
              <label htmlFor="destination-search">Where are you dreaming of?</label>
              <input
                id="destination-search"
                value={q}
                onChange={(event) => setQ(event.target.value)}
                placeholder="Try Goa, Bali or Dubai"
              />
            </div>
            <div>
              <label htmlFor="travel-date">When are you going?</label>
              <input id="travel-date" type="date" />
            </div>
            <div>
              <label htmlFor="guest-count">Who’s coming?</label>
              <select id="guest-count">
                <option>2 Adults</option>
                <option>1 Adult</option>
                <option>3 Adults</option>
                <option>4 Adults</option>
              </select>
            </div>
            <button className="search-btn" type="button">
              <Search size={18} />
              Find a trip
            </button>
          </div>
        </div>
        {bali && goa && (
          <div className="hero-scene" aria-hidden="true">
            <div className="hero-orbit" />
            <article className="trip-card trip-card-main">
              <img src={bali.image} alt="" />
              <div className="trip-card-caption">
                <span>YOUR NEXT CHAPTER</span>
                <strong>{bali.name}, {bali.country}</strong>
                <span className="trip-card-meta">
                  <MapPin size={13} />
                  Island days, your way
                </span>
              </div>
            </article>
            <article className="trip-card trip-card-small">
              <img src={goa.image} alt="" />
              <div>
                <span>WEEKEND FAVOURITE</span>
                <strong>{goa.name}, {goa.country}</strong>
              </div>
              <ArrowUpRight size={17} />
            </article>
            <div className="trip-note">
              <span className="trip-note-icon">✦</span>
              <span>
                <strong>Made for you</strong>
                <small>Places worth the journey</small>
              </span>
            </div>
          </div>
        )}
      </section>

      <section className="features">
        <div className="container feature-grid">
          <div><ShieldCheck /><b>Book with confidence</b><span>Clear details, no surprises</span></div>
          <div><Wallet /><b>More trip for your money</b><span>Options for different budgets</span></div>
          <div><Headphones /><b>Help when you need it</b><span>We’re here before and during your trip</span></div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="section-head">
            <div><span className="eyebrow">A LITTLE INSPIRATION</span><h2>Places worth the journey</h2></div>
            <Link to="/destinations" className="text-link">See every destination →</Link>
          </div>
          <div className="grid-3">{(q ? filtered : destinations).map((destination) => <DestinationCard key={destination.id} d={destination} />)}</div>
        </div>
      </section>

      <section className="section soft">
        <div className="container">
          <div className="section-head">
            <div><span className="eyebrow">A GOOD NIGHT’S SLEEP, WHEREVER YOU GO</span><h2>Stays that make the trip</h2></div>
            <Link to="/hotels" className="text-link">Browse all stays →</Link>
          </div>
          <div className="grid-4">{hotels.map((hotel) => <HotelCard key={hotel.id} h={hotel} />)}</div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="section-head">
            <div><span className="eyebrow">LESS PLANNING, MORE GOING</span><h2>Trips, already figured out</h2></div>
            <Link to="/packages" className="text-link">See all trip packages →</Link>
          </div>
          <div className="grid-4">{packages.map((travelPackage) => <PackageCard key={travelPackage.id} p={travelPackage} />)}</div>
        </div>
      </section>

      <section className="cta">
        <div className="container">
          <h2>Pick a place. We’ll help with the rest.</h2>
          <p>Find a destination, choose your stay and start looking forward to it.</p>
          <Link to="/destinations" className="btn">Find your somewhere</Link>
        </div>
      </section>
    </>
  );
}
