package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 * For storage of all possible integer modifiers in {@link EnumSet} and
 * {@link EnumMap} in declaring order.
 * @see Modifier
 */
public enum ModifierEnum {
	PUBLIC(Modifier.PUBLIC),
	PROTECTED(Modifier.PROTECTED),
	PRIVATE(Modifier.PRIVATE),
	ABSTRACT(Modifier.ABSTRACT),
	STATIC(Modifier.STATIC),
	FINAL(Modifier.FINAL),
	TRANSIENT(Modifier.TRANSIENT),
	VOLATILE(Modifier.VOLATILE),
	SYNCHRONIZED(Modifier.SYNCHRONIZED),
	NATIVE(Modifier.NATIVE),
	STRICT(Modifier.STRICT);
	
	private final int mod;
	private ModifierEnum(int mod) { this.mod = mod; }
	
	public int val() { return mod; }
}
