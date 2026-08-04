/**
 * Application configuration layer.
 *
 * <p>Holds Spring Boot {@code @Configuration} classes that wire the application:
 * beans, CORS, OpenAPI/Swagger, Jackson, async, scheduling, and integration
 * with external systems. Prefer keeping environment-specific values in
 * {@code application.yaml} and reading them via {@code @ConfigurationProperties}
 * or {@code @Value} from classes in this package.
 *
 * <p>Typical contents: security config (or link to {@code security}),
 * WebMvc/CORS config, OpenAPI config, Redis/Kafka clients, custom converters.
 */
package com.abhi.payments.config;
