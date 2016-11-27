package ru.ifmo.ctddev.pistyulga.common.hash;

import java.io.IOException;
import java.io.InputStream;

public class HashUtil {
	private HashUtil() {}
	
	/**
	 * Processes the streaming data with given hasher.
	 * Automatically closes the stream even when an error has occurred.
	 * @param inputStream - a stream of data being hashed
	 * @return Given hasher instance
	 * @throws IOException
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
