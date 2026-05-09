# Forma Backend

REST API for [Forma](https://devdad-forma.vercel.app) — an e-commerce storefront for furniture and home decor. Handles auth, product catalog, shopping cart/wishlist, address management, Stripe payments, and order tracking.

Built with Spring Boot 4, deployed on Railway.

## Tech Stack

- **Java 21** + **Spring Boot 4.0.6** (Web, Security, Data JPA, Validation)
- **PostgreSQL** — database
- **JWT (jjwt 0.13.0)** — auth via httpOnly cookies
- **Stripe Java SDK 32.0.0** — payment processing
- **Google OAuth2** — social login
- **Maven** — build tool (wrapper included)
- **Lombok** — because nobody likes writing getters

## Prerequisites

- Java 21+
- PostgreSQL running locally (or remote)
- Stripe account (for payment features)
- Google OAuth2 credentials (for social login)

## Local Setup

```bash
# clone the repo
git clone git@github.com:DevDad-Main/Forma-Backend.git
cd Forma-Backend

# create a local config for secrets (gitignored)
cp src/main/resources/application.properties src/main/resources/application-local.properties
```

Edit `application-local.properties` with your local credentials. At minimum:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<your-password>
jwt.secret=<some-random-string>
```

Then run:

```bash
./mvnw spring-boot:run -Dspring.profiles.active=local
```

The server starts on `http://localhost:8080`. Hibernate auto-creates tables via `ddl-auto=update`.

## Environment Variables (Production)

When deploying (Railway, etc.), set these as env vars:

| Variable | Description |
|---|---|
| `DATABASE_URL` | PostgreSQL connection string |
| `DATABASE_USERNAME` | DB user |
| `DATABASE_PASSWORD` | DB password |
| `JWT_SECRET` | Secret key for signing tokens |
| `JWT_EXPIRATION` | Token expiry in ms (default: 3600000) |
| `STRIPE_PUBLISHABLE_KEY` | Stripe publishable key |
| `STRIPE_SECRET_KEY` | Stripe secret key |
| `STRIPE_WEBHOOK_SECRET` | Stripe webhook signing secret |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret |

## API Overview

### Public

| Method | Path | What it does |
|---|---|---|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get a product |
| POST | `/api/auth/register` | Create account (email + password) |
| POST | `/api/auth/login` | Log in |
| POST | `/api/auth/logout` | Log out (clears JWT cookie) |
| POST | `/api/webhooks/stripe` | Stripe webhook endpoint |

### Authenticated (JWT in cookie or `Authorization: Bearer` header)

| Method | Path | What it does |
|---|---|---|
| GET | `/api/auth/me` | Current user profile |
| PUT | `/api/auth/profile` | Update name |
| GET | `/api/auth/address` | List addresses (max 3) |
| POST | `/api/auth/address` | Add address |
| PUT | `/api/auth/address` | Update address |
| DELETE | `/api/auth/address/{id}` | Delete address |
| GET | `/api/wishlist` | Get wishlist |
| POST | `/api/wishlist/add/{id}` | Add product to wishlist |
| DELETE | `/api/wishlist/remove/{id}` | Remove from wishlist |
| GET | `/api/orders` | Your orders |
| POST | `/api/payments/create-payment-intent` | Create Stripe PaymentIntent |

### Admin (requires `ADMIN` role)

| Method | Path | What it does |
|---|---|---|
| POST | `/api/admin/products` | Create product |
| PUT | `/api/admin/products/{id}` | Update product |
| POST | `/api/admin/products/seed` | Seed multiple products |

## Auth Flow

1. User registers or logs in via Google OAuth2
2. Server sets an httpOnly `jwt` cookie (`Secure`, `SameSite=None`)
3. Subsequent requests include the cookie automatically
4. `Authorization: Bearer <token>` header also works

CORS is configured for `https://devdad-forma.vercel.app` (the frontend). Credentialed requests (cookies) are supported.

## Stripe Webhooks

For local development, use the Stripe CLI to forward events:

```bash
stripe listen --forward-to localhost:8080/api/webhooks/stripe
```

Set the webhook signing secret as `STRIPE_WEBHOOK_SECRET` (or `stripe.api.webhook-secret` in properties).

## Google OAuth2

1. Create credentials at [Google Cloud Console](https://console.cloud.google.com)
2. Set authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
3. Add `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` to your config

## Testing

```bash
./mvnw test
```

Currently one smoke test — contributions welcome.

## License

MIT — see [LICENSE](LICENSE). Copyright 2026 Oliver Metz.
