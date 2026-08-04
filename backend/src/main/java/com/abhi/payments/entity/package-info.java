/**
 * Entity / persistence model layer.
 *
 * <p>Contains JPA {@code @Entity} classes that map to database tables. Fields
 * reflect stored data (ids, amounts, statuses, foreign keys). Entities are
 * owned by the persistence layer; do not expose them directly as API responses
 * — map to DTOs in the controller or a mapper instead.
 *
 * <p>Typical contents: {@code User}, {@code Account}, {@code Payment},
 * {@code Transaction}, enums for status, and JPA relations
 * ({@code @OneToMany}, {@code @ManyToOne}).
 */
package com.abhi.payments.entity;
