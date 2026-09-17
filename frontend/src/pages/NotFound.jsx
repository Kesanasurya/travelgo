import { Link } from "react-router-dom";
export default function NotFound() { return <section className="page-section"><div className="container narrow"><span className="eyebrow">404</span><h1>Page not found</h1><p>This route does not exist.</p><Link className="btn" to="/">Return home</Link></div></section>; }
