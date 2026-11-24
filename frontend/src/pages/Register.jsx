import { Link, useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { register } from "../api/authApi";

export default function Register() {
  const nav = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: ""
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const getErrorMessage = (err) => {
    const code = err.code || err.response?.data?.message || err.message || "";
    if (code.includes("email-already-in-use")) {
      return "This email is already registered. Please sign in instead.";
    }
    if (code.includes("weak-password")) {
      return "Password should be at least 6 characters.";
    }
    return err.response?.data?.message || err.message || "Unable to create account.";
  };

  const submit = async (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      setError("Passwords must match");
      return;
    }
    setLoading(true);
    setError("");
    try {
      const { data } = await register({
        name: form.name,
        email: form.email,
        password: form.password
      });
      localStorage.setItem("travelgo_token", data.token);
      localStorage.setItem("travelgo_user", JSON.stringify(data.user));
      const from = location.state?.from;
      nav(from ? `${from.pathname}${from.search || ""}` : "/");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <div className="logo">✈️ TravelGo</div>
        <h1>Create account</h1>
        <p>Start planning your next adventure.</p>
        {error && <div className="error">{error}</div>}
        <label>
          Full name
          <input
            required
            name="name"
            value={form.name}
            onChange={change}
            placeholder="Your name"
          />
        </label>
        <label>
          Email
          <input
            required
            name="email"
            type="email"
            value={form.email}
            onChange={change}
            placeholder="you@example.com"
          />
        </label>
        <label>
          Password
          <input
            required
            minLength="6"
            name="password"
            type="password"
            value={form.password}
            onChange={change}
            placeholder="At least 6 characters"
          />
        </label>
        <label>
          Confirm password
          <input
            required
            name="confirmPassword"
            type="password"
            value={form.confirmPassword}
            onChange={change}
            placeholder="Repeat your password"
          />
        </label>
        <button className="btn full" disabled={loading}>
          {loading ? "Creating..." : "Create Account"}
        </button>
        <div>
          Already registered? <Link to="/login" state={location.state}>Sign in</Link>
        </div>
      </form>
    </section>
  );
}