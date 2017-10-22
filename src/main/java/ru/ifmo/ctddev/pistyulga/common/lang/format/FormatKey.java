package ru.ifmo.ctddev.pistyulga.common.lang.format;

import java.util.EnumMap;

public interface FormatKey<E extends Enum<E> & FormatKey<E>> {
	
	<T> T cast(Object val);
	
	<T> T from(EnumMap<E, Object> params);
	
	void to(EnumMap<E, Object> params, Object val);
}
