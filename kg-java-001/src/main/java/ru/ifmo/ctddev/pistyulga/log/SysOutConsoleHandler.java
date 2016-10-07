package ru.ifmo.ctddev.pistyulga.log;

import java.util.logging.StreamHandler;

public class SysOutConsoleHandler extends StreamHandler {
	public SysOutConsoleHandler() {
		super(System.out, new SmallFormatter());
	}
}
