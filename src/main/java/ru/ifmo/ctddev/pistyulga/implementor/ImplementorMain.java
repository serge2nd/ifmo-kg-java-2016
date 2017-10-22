package ru.ifmo.ctddev.pistyulga.implementor;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;
import ru.ifmo.ctddev.pistyulga.common.log.LogService;

public class ImplementorMain {
	private static final Logger LOG = LogService.getLogger();
	
	private static void checkArgs(String[] args) {
		final int N_ARGS = 2;
		final String USAGE = "Usage: <program> <class_name> <jar_file>";
		if (args.length != N_ARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		checkArgs(args);
		
		String className = args[0],
				jarFileName = args[1];
		JarImpler impler = new Implementor();
		
		try {
			Path jarPath = Paths.get(jarFileName);
			Class<?> clazz = Class.forName(className);
			impler.implementJar(clazz, jarPath);
		} catch(InvalidPathException e) {
			LOG.log(Level.SEVERE, "Invalid path: " + jarFileName, e);
			System.exit(1);
		} catch(ClassNotFoundException e) {
			LOG.log(Level.SEVERE, "Class not found: " + className, e);
			System.exit(1);
		} catch(ImplerException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}
}
