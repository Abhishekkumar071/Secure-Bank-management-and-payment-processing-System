<h1 align="center">Secure Payment Processing & Orchestration Platform 🚀</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-orange.svg" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Database-PostgreSQL%20%7C%20H2-blue.svg" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Security-JWT%20%7C%20API%20Keys-red.svg" alt="Security">
  <img src="https://img.shields.io/badge/Architecture-Clean%20Layered-blueviolet.svg" alt="Architecture">
</p>

> A highly scalable, production-grade payment orchestration API built from scratch. It features dual-layer security, a robust idempotency engine, and a DAG-based state machine for payment execution, simulating real-world gateways like Stripe or Razorpay.

---

## 🏗️ System Architecture

The platform operates as an intermediate **Payment Orchestration Layer** between client applications (frontend/merchants) and actual payment gateways (Razorpay, Juspay, etc.).

```text
[ Client Software ] 
       │
       ▼ (REST API / X-Publishable-Key & X-Secret-Key)
[ Payment API Gateway ] 
       │
       ├──► [ Security Layer ] (O(1) Hash Lookup for M2M, JWT for B2C)
       │
       ├──► [ Idempotency Engine ] (Composite DB Locks, Prevents Double Charging)
       │
       ▼
[ Core Payment Service ] ──► [ Order State Machine (DAG: CREATED -> ATTEMPTED -> PAID) ]
       │
       ▼
[ External Gateway Providers ] (Razorpay / Mock Gateways)
       │
       ▼ (Asynchronous Updates)
[ Webhook Listener ] ──► [ Immutable Ledger & Settlement System ]
