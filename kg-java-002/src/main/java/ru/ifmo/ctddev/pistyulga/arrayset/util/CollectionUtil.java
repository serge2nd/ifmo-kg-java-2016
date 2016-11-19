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
	
	public static boolean isImmutable(Collection<?> c) {
		String className = c.getClass().getName();
		return unmodifiableCollectionClassNamePattern
				.matcher(className).matches();
	}
}
