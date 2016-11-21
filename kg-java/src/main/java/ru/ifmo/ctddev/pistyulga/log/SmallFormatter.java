package ru.ifmo.ctddev.pistyulga.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SmallFormatter extends Formatter {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
	
	@Override
	public String format(LogRecord record) {
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(dateFormat.format(new Date()));
		messageBuilder.append(' ');
		messageBuilder.append(record.getSourceClassName());
		messageBuilder.append(' ');
		messageBuilder.append(record.getLevel().toString());
		messageBuilder.append(": ");
		messageBuilder.append(record.getMessage());
		messageBuilder.append('\n');
		
		return messageBuilder.toString();
	}
}
