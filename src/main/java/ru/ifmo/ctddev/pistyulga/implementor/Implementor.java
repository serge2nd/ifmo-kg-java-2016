package ru.ifmo.ctddev.pistyulga.implementor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Objects;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;
import ru.ifmo.ctddev.pistyulga.common.lang.format.ElementFormatter;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatterFactory;
import ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatKeyImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatterFactoryImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ClassBuilder;

/**
 * Implementation of the {@link Impler} and {@link JarImpler} interfaces
 * @author Serge
 */
public class Implementor implements Impler, JarImpler {

	@Override
	public void implement(Class<?> token, Path root) throws ImplerException {
		Path packagePath = ClassUtil.getSubclassPath(token, root);
		if (packagePath == null) {
			throw new ImplerException("Cannot be a superclass: " + token.getName());
		}
		
		// If not an interface, look for accessible constructor
		Constructor<?> accessibleConstructor = null;
		if (!token.isInterface()) {
			accessibleConstructor = ImplementorUtil.getAccessibleConstructor(token);
			if (accessibleConstructor == null) {
				throw new ImplerException("Has not an accessible constructor: " + token.getName());
			}
		}
		
		// Resolving package path if not done
		if (packagePath == root) {
			packagePath = ClassUtil.resolvePackagePath(token, root);
		}

		// Class name & destination file path
		String classSimpleName = token.getSimpleName() + "Impl",
				className = token.getName() + "Impl";
		Path compilationUnitPath = packagePath.resolve(classSimpleName + ".java");
		
		// Create class builder
		ClassBuilder classBuilder =
				new ClassBuilder(className, Modifier.PUBLIC);
		
		// Superclass or interface & constructor
		if (token.isInterface()) {
			classBuilder.addInterface(token);
		} else {
			classBuilder.setSuperClass(token);
			if (accessibleConstructor.getParameters().length > 0 ||
				accessibleConstructor.getExceptionTypes().length > 0)
			{
				classBuilder.addEnclosedElement(
						ImplementorUtil.buildConstructorElement(accessibleConstructor));
			}
		}
		
		// Methods
		ImplementorUtil.buildUnimplementedMethods(token, classBuilder);
		
		// Get result & formatter
		TypeElement result = classBuilder.build();
		FormatterFactory<FormatKeyImpl> formatterFactory = FormatterFactoryImpl.getInstance();
		@SuppressWarnings("unchecked")
		ElementFormatter<FormatKeyImpl> formatter = (ElementFormatter<FormatKeyImpl>)
					formatterFactory.getFormatter(FormatKeyImpl.CLASS);
		
		// Create package hierarchy if not exists
		if (Files.notExists(packagePath)) {
			try {
				Files.createDirectories(packagePath);
			} catch (IOException e) {
				throw new ImplerException("Cannot create " + packagePath, e);
			}
		}
		
		// Write *.java
		try (Writer writer = new PrintWriter(compilationUnitPath.toString())) {
			Package pkg = token.getPackage();
			if (pkg != null) {
				writer.write("package ");
				writer.write(pkg.getName());
				writer.write(";\n");
			}
			formatter.format(result, writer, new EnumMap<>(FormatKeyImpl.class));
		} catch (IOException e) {
			throw new ImplerException("Cannot write " + compilationUnitPath, e);
		}
	}

	@Override
	public void implementJar(Class<?> token, Path jarFilePath) throws ImplerException {
		
		// Implement given class
		Path rootFolder = (jarFilePath.getNameCount() > 1) ?
				jarFilePath.subpath(0, jarFilePath.getNameCount() - 1) : Paths.get(".");
		this.implement(token, rootFolder);
		
		// Resolve necessary paths
		Package pkg = token.getPackage();
		String packageName = (pkg != null) ? pkg.getName() : "";
		Path packagePath = rootFolder.resolve(
				packageName.replace(CharPool.NAME_SEPARATOR, File.separatorChar));
		Path sourcePath = packagePath.resolve(token.getSimpleName() + "Impl.java");
		
		// Compile the implementation src
		JavaCompiler compiler = Objects.requireNonNull(
				ToolProvider.getSystemJavaCompiler(), "compiler");
		
		int exitCode = compiler.run(null, null, null, sourcePath.toString());
		if (exitCode != 0) {
			throw new ImplerException("Compilation error, see output. Exit code " + exitCode);
		}
		
		// Create JAR with source code and compiled class
		try {
			JarUtil.create(packagePath, packageName, jarFilePath.toString());
		} catch (IOException e) {
			throw new ImplerException("Cannot write " + jarFilePath.toString(), e);
		}
	}
}
