/**
 * Repository / data-access layer.
 *
 * <p>Defines Spring Data interfaces (usually extending {@code JpaRepository}
 * or {@code CrudRepository}) that talk to the database. Services use
 * repositories to load and save {@code entity} objects; repositories should
 * not contain business rules beyond query methods.
 *
 * <p>Typical contents: {@code UserRepository}, {@code PaymentRepository},
 * custom {@code @Query} methods, and optional custom repository implementations
 * for complex SQL/JPQL.
 */
package com.abhi.payments.repository;
