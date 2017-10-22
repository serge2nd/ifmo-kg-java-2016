package ru.ifmo.ctddev.pistyulga.common.format;

import java.util.Arrays;

public enum LinePad implements CharSequence {
	NO(0,'\0'),
	
	T1(1,'\t'),
	T2(2,'\t'),
	T3(3,'\t'),
	T4(4,'\t'),
	T5(5,'\t');
	
	private final String padding;
	private LinePad(int count, char c) {
		char[] charArray = new char[count];
		Arrays.fill(charArray, c);
		
		this.padding = new String(charArray);
	}
	
	@Override
	public String toString() { return padding; }
	
	@Override
	public int length() { return padding.length(); }

	@Override
	public char charAt(int index) { return padding.charAt(index); }

	@Override
	public CharSequence subSequence(int start, int end) { return padding.subSequence(start, end); }
}
