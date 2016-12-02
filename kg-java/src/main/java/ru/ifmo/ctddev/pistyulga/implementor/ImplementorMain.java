package ru.ifmo.ctddev.pistyulga.implementor;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.xml.bind.annotation.XmlValue;

import ru.ifmo.ctddev.pistyulga.common.lang.format.ElementFormatter;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatterFactory;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatKeyImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatterFactoryImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ClassBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ConstructorBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.MethodBuilder;

public class ImplementorMain {
	
	private static void checkArgs(String[] args) {
		final int N_ARGS = 2;
		final String USAGE = "Usage: <program> <class_name> <jar_file>";
		if (args.length != N_ARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws IOException {
		/*checkArgs(args);
		
		String className = args[0],
				jarFileName = args[1];
		
		try {
			Class<?> clazz = Class.forName(className);
			// TODO
		} catch(ClassNotFoundException e) {
			System.err.println("Class not found: " + className);
		}*/
		
		ExecutableElement e1 = new ConstructorBuilder(Modifier.PUBLIC)
				.addParameter(List.class, "l")
				.addParameter(String[].class, "ss")
				.addParameter(double[][].class, "d")
				.addParameter(int.class, "x")
				.addParameter(float[].class, "fs", true)
				.addThrownType(SQLException.class)
				.addThrownType(IOException.class)
				.build();
		
		ExecutableElement e2 = new MethodBuilder("func", void.class, Modifier.PUBLIC)
				.addAnnotation(Override.class)
				.addAnnotation(XmlValue.class)
				.addParameter(Object[].class, "args", true)
				.addThrownType(Throwable.class)
				.build();
		
		TypeElement e = new ClassBuilder("java.lang.Serge", Modifier.PUBLIC)
				.addInterface(AutoCloseable.class)
				.addInterface(SQLData.class)
				.addEnclosedElement(e1)
				.addEnclosedElement(e2)
				.build();
		
		StringBuilder b = new StringBuilder();
		FormatterFactory<FormatKeyImpl> fa = FormatterFactoryImpl.getInstance();
		ElementFormatter<FormatKeyImpl> f = (ElementFormatter<FormatKeyImpl>)
				fa.getFormatter(FormatKeyImpl.CLASS);
		
		f.format(e, b, new EnumMap<>(FormatKeyImpl.class));
		System.out.println(b);
	}
}
