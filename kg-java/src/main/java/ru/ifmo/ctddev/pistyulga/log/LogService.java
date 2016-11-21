package ru.ifmo.ctddev.pistyulga.log;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogService {
	private static final String LOG_CONFIG_PATH = "logging.properties";
	private static final String DEFAULT_LOGGER_NAME = "ru.ifmo.ctddev.pistyulga.walk.err";
	private static boolean initCalled;
	
	private LogService() {}
	
	private static void init() {
		if (!initCalled) {
			Logger globalLogger = Logger.getGlobal();
			
			try {
				LogManager.getLogManager().readConfiguration(LogService.class.getResourceAsStream(LOG_CONFIG_PATH));
			} catch(Exception e) {
				globalLogger.log(Level.SEVERE, "Cannot load logging configuration!", e);
			}
			
			initCalled = true;
		}
	}
	
	public static Logger getLogger() {
		if (!initCalled) {
			init();
		}
		return getLogger(DEFAULT_LOGGER_NAME);
	}
	
	public static Logger getLogger(String loggerName) {
		if (!initCalled) {
			init();
		}
		return Logger.getLogger(loggerName);
	}
}
