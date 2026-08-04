/**
 * Service / business-logic layer.
 *
 * <p>Contains {@code @Service} classes that implement use cases: payments,
 * transfers, account operations, validation rules, and orchestration across
 * repositories and external APIs. Controllers should stay thin and call
 * services; services should not depend on HTTP types ({@code HttpServletRequest},
 * {@code ResponseEntity}).
 *
 * <p>Typical contents: {@code PaymentService}, {@code TransferService},
 * transaction boundaries ({@code @Transactional}), domain rules, and calls to
 * repositories or other services.
 */
package com.abhi.payments.service;
