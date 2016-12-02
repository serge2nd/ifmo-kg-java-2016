package ru.ifmo.ctddev.pistyulga.implementor.lang.format;

import java.util.EnumMap;

import ru.ifmo.ctddev.pistyulga.common.format.LinePad;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatKey;

public enum FormatKeyImpl implements FormatKey<FormatKeyImpl> {
	CLASS(void.class, null),
	METHOD(void.class, null),
	IMPORTS(void.class, null),
	
	PAD(LinePad.class, LinePad.NO),
	
	BODY(String.class, "");
	
	private final Class<?> clazz;
	private final Object defaultValue;
	private <T> FormatKeyImpl(Class<T> clazz, T defaultValue) {
		this.clazz = clazz;
		this.defaultValue = defaultValue;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T cast(Object val) { return (T)clazz.cast((val != null) ? val : defaultValue);}

	@Override
	public <T> T from(EnumMap<FormatKeyImpl, Object> params) {
		return this.cast(params.get(this));
	}
	
	@Override
	public void to(EnumMap<FormatKeyImpl, Object> params, Object val) {
		params.put(this, this.cast(val));
	}
}
