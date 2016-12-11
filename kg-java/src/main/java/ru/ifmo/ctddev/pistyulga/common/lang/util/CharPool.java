package ru.ifmo.ctddev.pistyulga.common.lang.util;

/**
 * Contains:<br>
 * 1) char constants defining component type
 *    in specific Java array type name (e.g. "{@code [[Ljava.lang.Object;}");<br>
 * 2) the character used as a separator in the full name of a class (e.g. {@code java.lang.Object});<br>
 * 3) probably other useful chars.
 */
public class CharPool {
	/** Private constructor for this static class */
	private CharPool() {}
	
	public static final char
		BOOL   = 'Z',
		CHAR   = 'C',
		BYTE   = 'B',
		SHORT  = 'S',
		INT    = 'I',
		LONG   = 'J',
		FLOAT  = 'F',
		DOUBLE = 'D',
		CLASS  = 'L',
		NAME_SEPARATOR = '.';
}
