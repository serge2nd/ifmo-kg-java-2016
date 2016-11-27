package ru.ifmo.ctddev.pistyulga.common.lang.format;

public interface FormatterFactory<E extends Enum<E> & FormatKey<E>> {
	ElementFormatter<?> getFormatter(E type);
}
