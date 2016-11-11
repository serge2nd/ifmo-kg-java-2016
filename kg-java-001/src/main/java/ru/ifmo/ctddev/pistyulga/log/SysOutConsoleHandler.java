package ru.ifmo.ctddev.pistyulga.log;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class SysOutConsoleHandler extends StreamHandler {
	public SysOutConsoleHandler() {
		super(System.out, new SmallFormatter());
		super.setFilter(new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				return false;
				//return record.getLevel() != Level.SEVERE &&
				//		record.getLevel() != Level.WARNING;
			}
		});
	}
	
	@Override
	public synchronized void publish(LogRecord record) {
		super.publish(record);
		super.flush();
	}
}
