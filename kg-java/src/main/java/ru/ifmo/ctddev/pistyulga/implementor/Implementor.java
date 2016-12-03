package ru.ifmo.ctddev.pistyulga.implementor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.EnumMap;

import javax.lang.model.element.TypeElement;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;
import ru.ifmo.ctddev.pistyulga.common.lang.format.ElementFormatter;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatterFactory;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatKeyImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatterFactoryImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ClassBuilder;

public class Implementor implements Impler, JarImpler {

	@Override
	public void implement(Class<?> token, Path root) throws ImplerException {
		Path packagePath = ClassUtil.getSubclassPath(token, root);
		if (packagePath == null) {
			throw new ImplerException("Cannot be a superclass: " + token.getName());
		}
		
		// If not interface, check for accessible constructor
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
		String className = token.getSimpleName() + "Impl";
		Path compilationUnitPath = packagePath.resolve(className + ".java");
		
		// Superclass or interface & constructor
		ClassBuilder classBuilder = new ClassBuilder(className, Modifier.PUBLIC);
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
		
		// Write *.java
		try (Writer writer = new PrintWriter(compilationUnitPath.toString())) {
			writer.write("package ");
			writer.write(token.getPackage().getName());
			writer.write(";\n");
			
			formatter.format(result, writer, new EnumMap<>(FormatKeyImpl.class));
		} catch (IOException e) {
			throw new ImplerException(e);
		}
	}

	@Override
	public void implementJar(Class<?> token, Path root) throws ImplerException {
		// TODO Auto-generated method stub
		
	}

}
