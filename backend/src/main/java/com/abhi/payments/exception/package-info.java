/**
 * Exception handling layer.
 *
 * <p>Custom exception types for domain and API errors, plus a global handler
 * (usually {@code @RestControllerAdvice} / {@code @ExceptionHandler}) that
 * turns them into consistent HTTP status codes and error response bodies.
 * Keeps controllers free of repetitive try/catch and error formatting.
 *
 * <p>Typical contents: {@code ResourceNotFoundException},
 * {@code InsufficientBalanceException}, {@code UnauthorizedException},
 * {@code GlobalExceptionHandler}, and shared error response DTOs (or in
 * {@code dto}).
 */
package com.abhi.payments.exception;
