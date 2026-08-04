# Payment Platform Backend

Production-style payment processing platform backend built step by step with Spring Boot.

This project is being rebuilt from scratch as a payment platform foundation. The long-term goal is to support merchant APIs, payment orders, gateway integrations such as Razorpay or Juspay, webhooks, ledger entries, refunds, settlements, and dashboards.

## Current Status

Step 1 is complete:

- Clean Spring Boot application base
- New package root: `com.abhi.payments`
- Basic health check API
- Old banking-specific source code removed
- Build verified with Maven wrapper

## Tech Stack

- Java 25
- Spring Boot 4.0.3
- Maven

## Run Tests

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Health API

```http
GET /api/health
```

Example response:

```json
{
  "status": "UP",
  "service": "payment-platform",
  "timestamp": "2026-08-04T12:53:00Z"
}
```

## Roadmap

1. Clean project base and health API
2. Production-style package structure
3. Global API response and exception handling
4. Database setup and profiles
5. User authentication
6. Role-based access
7. Merchant module
8. API key system
9. Customer module
10. Payment order module
11. Idempotency
12. Payment sessions
13. Payment attempts
14. Gateway abstraction
15. Mock gateway provider
16. Razorpay or Juspay integration
17. Webhook processing
18. Ledger system
19. Refunds
20. Settlements
21. Audit logs
22. Rate limiting and hardening
23. Admin APIs
24. Reporting APIs
25. Tests
26. Docker and deployment preparation
27. API documentation
28. Frontend preparation

## Important Note

This project can be developed as a payment orchestration platform that integrates with licensed payment providers. Operating as an actual payment aggregator or payment gateway requires regulatory authorization, compliance, security audits, and banking/payment network partnerships.
