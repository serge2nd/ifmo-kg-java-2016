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

import ru.ifmo.ctddev.pistyulga.common.util.ZipUtil;

public class JarUtil {
	private JarUtil() {}
	
	private static final String DEFAULT_MANIFEST_CONTENT =
			"Manifest-Version: 1.0\n" +
			"Created-By: " + JarUtil.class.getName()
			+ "\n";
	
	public static void create(Path src, String packageName, String jarFilePath) throws IOException {
		Manifest man = new Manifest(
				new ByteArrayInputStream(DEFAULT_MANIFEST_CONTENT.getBytes("UTF-8")));
		
		create(src, packageName, jarFilePath, man);
	}
	
	public static void create(Path src, String packageName, String jarFileName, Manifest man) throws IOException {
		
		if (!packageName.isEmpty() && !SourceVersion.isName(packageName)) {
			throw new IllegalArgumentException("Invalid package: " + packageName);
		}
		
		Path packagePath = Paths.get(packageName.replace('.', File.separatorChar));
		if (!packageName.isEmpty() && !src.endsWith(packagePath)) {
			throw new IllegalArgumentException(
					"Package " + packageName + " and source " + src + " are incompatible");
		}
		
		try(JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFileName), man)) {
			ZipUtil.addRecursively(src, packagePath, jarStream);
		}
	}
}
