package ru.ifmo.ctddev.pistyulga.common.hash;

import java.util.Arrays;

/**
 * @see <a href="https://en.wikipedia.org/wiki/MD5">MD5 (en.wikipedia.org)</a>
 */
public class MD5LowMemHasher implements LowMemHasher {
	private static final String EMPTY_HASH_STR = "00000000000000000000000000000000";
	
	public static final String getEmptyHashStr() {
		return EMPTY_HASH_STR;
	}
	
	// Initial value
	private static final int A = 0x67452301,
							 B = 0xEFCDAB89,
							 C = 0x98BADCFE,
							 D = 0x10325476;
	
	// Magic constants
	private static final byte[] S = { 7, 12, 17, 22,
									  5,  9, 14, 20,
									  4, 11, 16, 23,
									  6, 10, 15, 21 };
	
	private static final int[] T = {
			0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
			0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
			0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d,  0x2441453, 0xd8a1e681, 0xe7d3fbc8,
			0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
			0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
			0x289b7ec6, 0xeaa127fa, 0xd4ef3085,  0x4881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
			0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
			0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
	};
	
	// Destination buf
	private int[] buf = { A, B, C, D };
	private int[] words = { 0, 0, 0, 0,
							0, 0, 0, 0,
							0, 0, 0, 0,
							0, 0, 0, 0 };
	
	private long bytesCounter;
	private boolean isFinished;
	
	@Override
	public LowMemHasher appendByte(int b) {
		if (isFinished) {
			clear();
		}
		
		int iBytesCount = (int)bytesCounter;
		b &= 0xFF;
		
		int wordNum = (iBytesCount & 0x3F) >>> 2;
		
		if ((iBytesCount & 3) == 0) {
			words[wordNum] = 0;
		}
		words[wordNum] |= (b << ((iBytesCount & 3) << 3));
		
		if ((++iBytesCount & 0x3F) == 0) {
			processBlock();
		}
		bytesCounter++;

		return this;
	}
	
	@Override
	public LowMemHasher clear() {
		buf[0] = A; buf[1] = B; buf[2] = C; buf[3] = D;
		bytesCounter = 0L; isFinished = false;
		return this;
	}
	
	/**
	 * @throws IllegalStateException - if {@link #finish()} was not called before
	 */
	public int[] toIntArray() {
		if (!isFinished) {
			throw new IllegalStateException("You must call finish() at first!");
		}
		
		int[] result = Arrays.copyOf(buf, buf.length);
		
		return result;
	}
	
	/**
	 * @throws IllegalStateException - if {@link #finish()} was not called before
	 */
	public byte[] toByteArray() {
		if (!isFinished) {
			throw new IllegalStateException("You must call finish() at first!");
		}
		
		int bufBytesCount = buf.length * 4;
		
		byte[] result = new byte[bufBytesCount];
		
		for(int i = 0; i < bufBytesCount; i++) {
			result[i] = (byte)(buf[i >>> 2] >>> ((3 - (i & 3)) << 3));
		}
		
		return result;
	}
	
	/**
	 * @throws IllegalStateException - if {@link #finish()} was not called before
	 */
	@Override
	public String toString() {
		if (!isFinished) {
			throw new IllegalStateException("You must call finish() at first!");
		}
		
		StringBuilder result = new StringBuilder(buf.length * 8);
		
		for(int i = 0; i < buf.length; i++) {
			
			// Producing the little-endian value
			String intFrameStr =
					Integer.toHexString(
					   		 (buf[i] >>> 24) |
					   		((buf[i] >>> 8) & 0x00FF00) |
					   		((buf[i] << 8) & 0xFF0000) |
					   		 (buf[i] << 24));
			int leadingZerosCount = Integer.SIZE/4 - intFrameStr.length();
			
			while (leadingZerosCount-- > 0) {
				result.append('0');
			}
			result.append(intFrameStr);
		}
		
		return result.toString();
	}
	
	@Override
	public LowMemHasher finish() {
		if (isFinished) {
			return this;
		}
		
		// Padding the length of data up to 64 bits fewer
		// than a multiple of 512 with bits 1 0 0 0 0 0 ... 0
		long beforePaddingBytesCount = bytesCounter;
		int bytesModulo64 = ((int)bytesCounter) & 0x3F;
		int paddingBytesCount =
				(bytesModulo64 < 56) ? (56 - bytesModulo64) : (120 - bytesModulo64);
		
		appendByte(0x80); paddingBytesCount--;
		for(int i = 0; i < paddingBytesCount; i++) {
			appendByte(0);
		}
		
		// Writing data length in bits to end
		long bitsCount = beforePaddingBytesCount * 8;
		for (int k = 0; k < 64; k += 8) {
			appendByte((int)(bitsCount >>> k));
		}
		
		isFinished = true;
		
		return this;
	}
	
	/**
	 * Modifies the state with the next accumulated 512-bit block
	 */
	private void processBlock() {
		int a = buf[0], b = buf[1], c = buf[2], d = buf[3];
		
		// *** Step 1 ***
		a = oneRoundF(a, b, c, d, words[0],  T[0],  S[0]);
		d = oneRoundF(d, a, b, c, words[1],  T[1],  S[1]);
		c = oneRoundF(c, d, a, b, words[2],  T[2],  S[2]);
		b = oneRoundF(b, c, d, a, words[3],  T[3],  S[3]);
		
		a = oneRoundF(a, b, c, d, words[4],  T[4],  S[0]);
		d = oneRoundF(d, a, b, c, words[5],  T[5],  S[1]);
		c = oneRoundF(c, d, a, b, words[6],  T[6],  S[2]);
		b = oneRoundF(b, c, d, a, words[7],  T[7],  S[3]);
		
		a = oneRoundF(a, b, c, d, words[8],  T[8],  S[0]);
		d = oneRoundF(d, a, b, c, words[9],  T[9],  S[1]);
		c = oneRoundF(c, d, a, b, words[10], T[10], S[2]);
		b = oneRoundF(b, c, d, a, words[11], T[11], S[3]);
		
		a = oneRoundF(a, b, c, d, words[12], T[12], S[0]);
		d = oneRoundF(d, a, b, c, words[13], T[13], S[1]);
		c = oneRoundF(c, d, a, b, words[14], T[14], S[2]);
		b = oneRoundF(b, c, d, a, words[15], T[15], S[3]);
		
		// *** Step 2 ***
		a = oneRoundG(a, b, c, d, words[1],  T[16], S[4]);
		d = oneRoundG(d, a, b, c, words[6],  T[17], S[5]);
		c = oneRoundG(c, d, a, b, words[11], T[18], S[6]);
		b = oneRoundG(b, c, d, a, words[0],  T[19], S[7]);
		
		a = oneRoundG(a, b, c, d, words[5],  T[20], S[4]);
		d = oneRoundG(d, a, b, c, words[10], T[21], S[5]);
		c = oneRoundG(c, d, a, b, words[15], T[22], S[6]);
		b = oneRoundG(b, c, d, a, words[4],  T[23], S[7]);
		
		a = oneRoundG(a, b, c, d, words[9],  T[24], S[4]);
		d = oneRoundG(d, a, b, c, words[14], T[25], S[5]);
		c = oneRoundG(c, d, a, b, words[3],  T[26], S[6]);
		b = oneRoundG(b, c, d, a, words[8],  T[27], S[7]);
		
		a = oneRoundG(a, b, c, d, words[13], T[28], S[4]);
		d = oneRoundG(d, a, b, c, words[2],  T[29], S[5]);
		c = oneRoundG(c, d, a, b, words[7],  T[30], S[6]);
		b = oneRoundG(b, c, d, a, words[12], T[31], S[7]);
		
		// *** Step 3 ***
		a = oneRoundH(a, b, c, d, words[5],  T[32], S[8]);
		d = oneRoundH(d, a, b, c, words[8],  T[33], S[9]);
		c = oneRoundH(c, d, a, b, words[11], T[34], S[10]);
		b = oneRoundH(b, c, d, a, words[14], T[35], S[11]);
		
		a = oneRoundH(a, b, c, d, words[1],  T[36], S[8]);
		d = oneRoundH(d, a, b, c, words[4],  T[37], S[9]);
		c = oneRoundH(c, d, a, b, words[7],  T[38], S[10]);
		b = oneRoundH(b, c, d, a, words[10], T[39], S[11]);
		
		a = oneRoundH(a, b, c, d, words[13], T[40], S[8]);
		d = oneRoundH(d, a, b, c, words[0],  T[41], S[9]);
		c = oneRoundH(c, d, a, b, words[3],  T[42], S[10]);
		b = oneRoundH(b, c, d, a, words[6],  T[43], S[11]);
		
		a = oneRoundH(a, b, c, d, words[9],  T[44], S[8]);
		d = oneRoundH(d, a, b, c, words[12], T[45], S[9]);
		c = oneRoundH(c, d, a, b, words[15], T[46], S[10]);
		b = oneRoundH(b, c, d, a, words[2],  T[47], S[11]);
		
		// *** Step 4 ***
		a = oneRoundI(a, b, c, d, words[0],  T[48], S[12]);
		d = oneRoundI(d, a, b, c, words[7],  T[49], S[13]);
		c = oneRoundI(c, d, a, b, words[14], T[50], S[14]);
		b = oneRoundI(b, c, d, a, words[5],  T[51], S[15]);
		
		a = oneRoundI(a, b, c, d, words[12], T[52], S[12]);
		d = oneRoundI(d, a, b, c, words[3],  T[53], S[13]);
		c = oneRoundI(c, d, a, b, words[10], T[54], S[14]);
		b = oneRoundI(b, c, d, a, words[1],  T[55], S[15]);
		
		a = oneRoundI(a, b, c, d, words[8],  T[56], S[12]);
		d = oneRoundI(d, a, b, c, words[15], T[57], S[13]);
		c = oneRoundI(c, d, a, b, words[6],  T[58], S[14]);
		b = oneRoundI(b, c, d, a, words[13], T[59], S[15]);
		
		a = oneRoundI(a, b, c, d, words[4],  T[60], S[12]);
		d = oneRoundI(d, a, b, c, words[11], T[61], S[13]);
		c = oneRoundI(c, d, a, b, words[2],  T[62], S[14]);
		b = oneRoundI(b, c, d, a, words[9],  T[63], S[15]);
		
		buf[0] += a; buf[1] += b; buf[2] += c; buf[3] += d;
	}
	
	// Magic functions
	private static int F(int x, int y, int z) {
		return (x & y) | (~x & z);
	}
	
	private static int G(int x, int y, int z) {
		return (x & z) | (~z & y);
	}
	
	private static int H(int x, int y, int z) {
		return x ^ y ^ z;
	}
	
	private static int I(int x, int y, int z) {
		return y ^ (~z | x);
	}
	
	private static int oneRoundF(int a, int b, int c, int d, int x, int t, int s) {
		int y = a + F(b, c, d) + x + t;
		return b + ((y << s) | (y >>> (32 - s)));
	}
	
	private static int oneRoundG(int a, int b, int c, int d, int x, int t, int s) {
		int y = a + G(b, c, d) + x + t;
		return b + ((y << s) | (y >>> (32 - s)));
	}
	
	private static int oneRoundH(int a, int b, int c, int d, int x, int t, int s) {
		int y = a + H(b, c, d) + x + t;
		return b + ((y << s) | (y >>> (32 - s)));
	}
	
	private static int oneRoundI(int a, int b, int c, int d, int x, int t, int s) {
		int y = a + I(b, c, d) + x + t;
		return b + ((y << s) | (y >>> (32 - s)));
	}
}
