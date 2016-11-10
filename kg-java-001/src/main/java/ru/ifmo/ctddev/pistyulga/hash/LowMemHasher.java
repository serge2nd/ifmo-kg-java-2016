package ru.ifmo.ctddev.pistyulga.hash;

public interface LowMemHasher {
	
	/**
	 * If finish() was called before, clear() is called at first if was not
	 * @param bytes
	 * @return This hasher impl
	 */
	LowMemHasher appendByte(int b);
	
	/**
	 * Sets the hasher to the finished mode after what one can get the result
	 * @return This hasher impl
	 */
	LowMemHasher finish();
	
	/**
	 * Resets all data fields to initial state
	 * @return This hasher impl
	 */
	LowMemHasher clear();
}
