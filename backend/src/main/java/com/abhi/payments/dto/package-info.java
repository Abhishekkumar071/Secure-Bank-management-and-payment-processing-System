/**
 * DTO (Data Transfer Object) layer.
 *
 * <p>Request and response shapes for the REST API. DTOs decouple the public
 * contract from database entities so you can change tables without breaking
 * clients, and avoid leaking sensitive fields. Often validated with
 * Jakarta Bean Validation ({@code @NotNull}, {@code @Email}, {@code @Size}).
 *
 * <p>Typical contents: {@code CreatePaymentRequest}, {@code PaymentResponse},
 * {@code ApiErrorResponse}, login/register payloads, and nested view models.
 */
package com.abhi.payments.dto;
