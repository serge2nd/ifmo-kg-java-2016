package ru.ifmo.ctddev.pistyulga.hash;

import java.util.Arrays;

public class FNV1ALowMemHasher implements LowMemHasher {
	public static enum Size {
		_32BIT(0), _64BIT(1), _128BIT(2), _256BIT(3), _512BIT(4), _1024BIT(5);
		private int ind;
		Size(int ind) { this.ind = ind; }
	}
	
	private static final long LONG_MASK = 0xFFFFFFFFL;
	
	private static int[][] primeNumbers = {
			
			// 2^24 + 2^8 + 0x93
			{ 0x01000193 },
			
			// 2^40 + 2^8 + 0xb3
			{ 0x1b3, 0x100 },
			
			// 2^88 + 2^8 + 0x3b
			{ 0x13b, 0, 0x01000000 },
			
			// 2^168 + 2^8 + 0x63
			{ 0x163, 0, 0, 0, 0, 0x0100 },
			
			// 2^344 + 2^8 + 0x57
			{ 0x157, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x01000000},
			
			// 2^680 + 2^8 + 0x8d
			{ 0x18d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x0100 }
	};
	
	private static int[][] initialValues = {
			{ 0x811c9dc5 },
			{ 0x84222325, 0xcbf29ce4 },
			{ 0x6295c58d, 0x62b82175, 0x07bb0142, 0x6c62272e },
			{ 0xcaee0535, 0x1023b4c8, 0x47b6bbb3, 0xc8b15368, 0xc4e576cc, 0x2d98c384, 0xaac55036, 0xdd268dbc },
			
			{ 0x4afe9fd9, 0xac982aac, 0x5f56e34b, 0x18203641, 0x42dbe7ce, 0x2ea79bc9, 0x34c192f6, 0xe948f68a,
			  0x00000d21, 0x00000000, 0xc9000000, 0xac87d059, 0x309990ac, 0xdca1e50f, 0x171f4416, 0xb86db0b1 },
			
			{ 0x71ee90b3, 0xaff4b16c, 0xc6a93b21, 0x6bde8cc9, 0xc005ae55, 0x555f256c, 0x2734510a, 0xeb6e7380,
			  0x0004c6d7, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
			  0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x9a21d900, 0xda3674da, 0x6c3bf34e,
			  0x23fdada1, 0x4b29fc42, 0x591028b7, 0x32e56d5a, 0x758ecc4d, 0x005f7a76, 0x00000000, 0x00000000 }
	};
	
	private int[] primeNum, hash, buf;
	private boolean isFinished;
	
	public FNV1ALowMemHasher(Size size) {
		int optionNum = size.ind;
		primeNum = primeNumbers[optionNum];
		int[] initialValue = initialValues[optionNum];
		
		hash = Arrays.copyOf(initialValue, initialValue.length);
		buf = new int[hash.length];
	}
	
	@Override
	public LowMemHasher appendByte(int b) {
		if (isFinished) {
			clear();
		}
		
		b &= 0xFF;
		hash[0] ^= b;
		multiplyToBuf(hash, primeNum);
		
		for (int i = 0; i < buf.length; i++) {
			hash[i] = buf[i]; buf[i] = 0;
		}
		
		return this;
	}
	
	private void multiplyToBuf(int[] x, int[] y) {
		int bufSize = (x.length < buf.length) ? x.length : buf.length;
		long carry = 0;
		
		for (int j = 0; j < bufSize; j++) {
			long product = (x[j] & LONG_MASK) *
						   (y[0] & LONG_MASK)
						   + carry;
			
			buf[j] = (int)product;
			carry = product >>> 32;
		}
		if (buf.length > bufSize)
			buf[bufSize] = (int)carry;
		
		for (int i = 1; i < y.length; i++) {
			carry = 0;
			
			for (int j = 0, k = i; j < bufSize && k < buf.length; j++, k++) {
				long product = (x[j] & LONG_MASK) *
							   (y[i] & LONG_MASK)
							   + carry;
				
				product += (buf[k] & LONG_MASK);
				buf[k] = (int)product;
				carry = product >>> 32;
			}
			if (buf.length > bufSize + i)
				buf[bufSize + i] = (int)carry;
		}
	}
	
	@Override
	public LowMemHasher finish() {
		isFinished = true;
		return this;
	}
	
	@Override
	public LowMemHasher clear() {
		int optionNum = Integer.numberOfTrailingZeros(hash.length);
		
		for (int i = 0; i < hash.length; i++) {
			hash[i] = initialValues[optionNum][i];
			buf[i] = 0;
		}
		isFinished = false;
		
		return this;
	}
	
	@Override
	public String toString() {
		if (!isFinished) {
			throw new IllegalStateException("You must call finish() at first!");
		}

		StringBuilder result = new StringBuilder(hash.length * 8);
		
		for (int i = hash.length - 1; i >= 0; i--) {
			String intFrameStr = Integer.toHexString(hash[i]);
			int leadingZerosCount = Integer.SIZE/4 - intFrameStr.length();
			
			while (leadingZerosCount-- > 0) {
				result.append('0');
			}
			result.append(intFrameStr);
		}
		
		return result.toString();
	}
}
