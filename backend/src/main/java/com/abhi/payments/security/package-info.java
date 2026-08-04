/**
 * Security layer.
 *
 * <p>Authentication and authorization for the payment platform: Spring Security
 * filter chain, JWT (or session) handling, password encoding, user details
 * loading, and method/URL access rules. Protects endpoints so only allowed
 * roles/users can call sensitive payment APIs.
 *
 * <p>Typical contents: {@code SecurityConfig}, {@code JwtService},
 * {@code JwtAuthenticationFilter}, {@code UserDetailsService} implementation,
 * CORS/CSRF choices, and role/permission constants.
 */
package com.abhi.payments.security;
