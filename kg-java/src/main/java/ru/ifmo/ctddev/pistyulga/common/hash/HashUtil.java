package ru.ifmo.ctddev.pistyulga.common.hash;

import java.io.IOException;
import java.io.InputStream;

public class HashUtil {
	/** Private constructor for this static class */
	private HashUtil() {}
	
	/**
	 * Processes the streaming data with the given hasher.
	 * Automatically closes the stream even when an error has occurred.
	 * @param inputStream - a stream of data being hashed
	 * @return Given hasher instance
	 * @throws IOException if I/O error has occurred
	 */
	public static LowMemHasher fromStream(LowMemHasher hasher, InputStream inputStream) throws IOException {
		try(InputStream in = inputStream) {
			int b;
			while ((b = in.read()) != -1) {
				hasher.appendByte(b);
			}
		}
		
		return hasher;
	}
}
