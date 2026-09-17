# TravelGo API

Base URL: `http://localhost:5000/api`

Public endpoints: `GET /health`, `POST /auth/register`, `POST /auth/login`, `GET /destinations`, `GET /destinations/{id}`, `GET /hotels`, `GET /hotels/{id}`, `GET /packages`, and `GET /packages/{id}`.

Authenticated endpoints use `Authorization: Bearer <JWT>`: `GET /auth/me`, `POST /bookings`, `GET /bookings/my`, `GET /bookings/{id}`, and `PUT /bookings/{id}/cancel`.

Admin-only mutations are the POST/PUT/DELETE destination, hotel, and package endpoints plus `GET /bookings/admin`. Booking totals are calculated server-side as package price multiplied by guests. Errors return timestamp, status, message, and path JSON.
