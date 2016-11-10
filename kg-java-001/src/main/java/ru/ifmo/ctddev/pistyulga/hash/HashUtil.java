package ru.ifmo.ctddev.pistyulga.hash;

import java.io.IOException;
import java.io.InputStream;

public class HashUtil {
	private HashUtil() {}
	
	/**
	 * @param inputStream - a stream of data being hashed
	 * @return Given hasher instance
	 */
	public static LowMemHasher fromStream(LowMemHasher hasher, InputStream inputStream) throws IOException {
		try(InputStream in = inputStream) {
			int b;
			while ((b = in.read()) != -1) {
				hasher.appendByte(b);
			}
		} catch(IOException e) {
			throw e;
		}
		
		return hasher;
	}
}
