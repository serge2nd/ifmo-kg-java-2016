package ru.ifmo.ctddev.pistyulga.helloudp.util;

import java.util.concurrent.ThreadFactory;

public class EnumeratedThreadFactory implements ThreadFactory {
	private EnumeratedThreadFactory(int maxNumber) {
		this.maxNumber = maxNumber;
	}
	
	public static ThreadFactory getInstance(int maxNumber) {
		return new EnumeratedThreadFactory(maxNumber);
	}
	
	private final int maxNumber;
	private int currNumber = 0;
	
	@Override
	public Thread newThread(Runnable r) {
		if (currNumber == maxNumber) {
			return null;
		}
		return new Thread(r, String.valueOf(currNumber++));
	}
}
