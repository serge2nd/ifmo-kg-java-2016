package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.lang.reflect.Modifier;

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
