package ru.ifmo.ctddev.pistyulga.common.builder;

/**
 * An interface for the {@code Builder} pattern
 * @param <T> - result type
 * @author Serge
 */
public interface Builder<T> {
	T build();
}
