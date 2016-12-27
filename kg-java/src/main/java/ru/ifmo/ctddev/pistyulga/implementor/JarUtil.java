package ru.ifmo.ctddev.pistyulga.implementor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.lang.model.SourceVersion;

import ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool;
import ru.ifmo.ctddev.pistyulga.common.util.ZipUtil;

/**
 * Utility methods working with JAR files
 */
public class JarUtil {
	/** Private constructor for this static class */
	private JarUtil() {}
	
	/** Default content of MANIFEST.MF: manifest version & creation tool ({@code JarUtil}) */
	private static final String DEFAULT_MANIFEST_CONTENT =
			"Manifest-Version: 1.0\n" +
			"Created-By: " + JarUtil.class.getName()
			+ "\n";
	
	/**
	 * Equivalent to {@link #create(Path, String, String, Manifest) create(src, packageName, jarFileName, DEFAULT_MANIFEST_CONTENT)}
	 * @param src - a path where to look for sources
	 * @param packageName - root package name
	 * @param jarFileName - destination file name
	 * @throws IOException if I/O error has occurred
	 * @see #DEFAULT_MANIFEST_CONTENT
	 */
	public static void create(Path src, String packageName, String jarFileName) throws IOException {
		Manifest man = new Manifest(
				new ByteArrayInputStream(DEFAULT_MANIFEST_CONTENT.getBytes("UTF-8")));
		
		create(src, packageName, jarFileName, man);
	}
	
	/**
	 * Creates JAR file with given path using {@link ZipUtil}
	 * @param src - a path where to look for sources
	 * @param packageName - root package name
	 * @param jarFileName - destination file name
	 * @param man - a {@link Manifest} instance
	 * @throws IOException if I/O error has occurred
	 */
	public static void create(Path src, String packageName, String jarFileName, Manifest man) throws IOException {
		
		if (!packageName.isEmpty() && !SourceVersion.isName(packageName)) {
			throw new IllegalArgumentException("Invalid package: " + packageName);
		}
		
		Path packagePath = Paths.get(packageName.replace(CharPool.NAME_SEPARATOR, File.separatorChar));
		if (!packageName.isEmpty() && !src.endsWith(packagePath)) {
			throw new IllegalArgumentException(
					"Package " + packageName + " and source " + src + " are incompatible");
		}
		
		try(JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFileName), man)) {
			ZipUtil.addRecursively(src, packagePath, jarStream);
		}
	}
}
