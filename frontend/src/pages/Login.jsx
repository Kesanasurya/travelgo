import { Link, useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { login } from "../api/authApi";

export default function Login() {
  const nav = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const getErrorMessage = (err) => {
    const code = err.code || err.response?.data?.message || err.message || "";
    if (
      code.includes("invalid-credential") ||
      code.includes("wrong-password") ||
      code.includes("user-not-found")
    ) {
      return "Invalid email or password. Please create an account if you are new.";
    }
    if (code.includes("too-many-requests")) {
      return "Too many attempts. Please try again later.";
    }
    return err.response?.data?.message || err.message || "Unable to sign in. Check your details.";
  };

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const { data } = await login({ email, password });
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
        <h1>Welcome back</h1>
        <p>Sign in to manage your trips.</p>
        {error && <div className="error">{error}</div>}
        <label>
          Email
          <input
            required
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
          />
        </label>
        <label>
          Password
          <input
            required
            minLength="6"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Your password"
          />
        </label>
        <button className="btn full" disabled={loading}>
          {loading ? "Signing in..." : "Sign In"}
        </button>
        <div>
          New here? <Link to="/register" state={location.state}>Create an account</Link>
        </div>
      </form>
    </section>
  );
}