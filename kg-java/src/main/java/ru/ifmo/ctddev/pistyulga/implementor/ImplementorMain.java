package ru.ifmo.ctddev.pistyulga.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;

public class ImplementorMain {
	
	private static void checkArgs(String[] args) {
		final int N_ARGS = 2;
		final String USAGE = "Usage: <program> <class_name> <jar_file>";
		if (args.length != N_ARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws ImplerException {
		checkArgs(args);
		
		String className = args[0],
				jarFileName = args[1];
		
		try {
			Class<?> clazz = Class.forName(className);
			// TODO
		} catch(ClassNotFoundException e) {
			System.err.println("Class not found: " + className);
		}
	}
}
