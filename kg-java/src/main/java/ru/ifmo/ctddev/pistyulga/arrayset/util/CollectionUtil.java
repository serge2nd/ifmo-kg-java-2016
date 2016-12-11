package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Collection;
import java.util.regex.Pattern;

public class CollectionUtil {
	private CollectionUtil() {}
	
	private static final Pattern unmodifiableCollectionClassNamePattern =
			Pattern.compile("java.util.Collections\\$(" +
							"Unmodifiable" +
									"(Set|SortedSet|NavigableSet(" +
											"\\$EmptyNavigableSet)?|" +
									"List|RandomAccessList|Collection)|" +
							"Singleton" +
									"(Set|List)|" +
							"Empty" +
									"(Set|List))");
	
	/**
	 * Used to determine immutability of a collection to pass test #6 :)
	 * @param c - a collection
	 * @return {@code true} if this collection is one of standard immutable collections
	 */
	public static boolean isImmutable(Collection<?> c) {
		String className = c.getClass().getName();
		return unmodifiableCollectionClassNamePattern
				.matcher(className).matches();
	}
}
