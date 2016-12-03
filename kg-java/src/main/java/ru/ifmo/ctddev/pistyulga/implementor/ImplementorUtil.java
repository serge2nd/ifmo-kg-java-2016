package ru.ifmo.ctddev.pistyulga.implementor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;

import ru.ifmo.ctddev.pistyulga.common.lang.util.MethodUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.builder.ExecutableBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ClassBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ConstructorBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.MethodBuilder;

public class ImplementorUtil {
	/** Private constructor for this static class */
	private ImplementorUtil() {}
	
	public static Constructor<?> getAccessibleConstructor(Class<?> clazz) {
		try {
			Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
			if (!Modifier.isPrivate(defaultConstructor.getModifiers())) {
				return defaultConstructor;
			}
		} catch (NoSuchMethodException e) {}
		
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (!Modifier.isPrivate(c.getModifiers())) {
				return c;
			}
		}
		
		return null;
	}
	
	public static void buildUnimplementedMethods(Class<?> clazz, ClassBuilder classBuilder) {
		
		Set<String> signatures = new HashSet<>();
		List<Class<?>> ancestors = new ArrayList<>();
		Queue<Class<?>> interfaces = new LinkedList<>();
		
		Class<?> currClass = clazz;
		while (currClass != null && Modifier.isAbstract(currClass.getModifiers())) {
			buildUnimplementedMethods(classBuilder, currClass, ancestors, signatures);
			
			interfaces.addAll(Arrays.asList(currClass.getInterfaces()));
			ancestors.add(currClass);
			
			while (!interfaces.isEmpty()) {
				Class<?> iface = interfaces.poll();
				buildUnimplementedMethods(classBuilder, iface, ancestors, signatures);
				interfaces.addAll(Arrays.asList(iface.getInterfaces()));
			}
			
			currClass = currClass.getSuperclass();
		}
	}
	
	private static void buildUnimplementedMethods(
			ClassBuilder classBuilder, Class<?> clazz, List<Class<?>> subClasses, Set<String> signatures)
	{
		Method[] methods = clazz.getDeclaredMethods();
		
		for (Method method : methods) {
			if (Modifier.isAbstract(method.getModifiers())) {
				Method targetMethod = searchMethod(method, subClasses);
				
				if (Modifier.isAbstract(targetMethod.getModifiers())) {
					String methodSignature = MethodUtil.getSignature(targetMethod);
					boolean noSignature = signatures.add(methodSignature);
					
					if (noSignature) {
						classBuilder.addEnclosedElement(buildMethodElement(targetMethod));
					}
				}
			}
		}
	}
	
	public static ExecutableElement buildMethodElement(Method method) {
		MethodBuilder methodBuilder =
				new MethodBuilder(method.getName(), method.getReturnType(), Modifier.PUBLIC);
		
		return addThrownAndArgs(method, methodBuilder.addAnnotation(Override.class))
				.setBody(getMethodBody(method))
				.build();
	}
	
	public static ExecutableElement buildConstructorElement(Constructor<?> c) {
		ConstructorBuilder cBuilder = new ConstructorBuilder(Modifier.PUBLIC);
		
		return addThrownAndArgs(c, cBuilder)
				.setBody(getConstructorBody(c))
				.build();
	}
	
	private static Method searchMethod(Method method, List<Class<?>> subClasses) {
		String methodName = method.getName();
		Class<?>[] paramTypes = method.getParameterTypes();
		
		Method targetMethod = method;
		for (int i = subClasses.size() - 1; i >= 0; i--) {
			try {
				Class<?> next = subClasses.get(i);
				targetMethod = next.getDeclaredMethod(methodName, paramTypes);
				
				if (!Modifier.isAbstract(targetMethod.getModifiers())) {
					break;
				}
			} catch (NoSuchMethodException e) {}
		}
		
		return targetMethod;
	}
	
	private static ExecutableBuilder addThrownAndArgs(Executable executable, ExecutableBuilder b) {
		for (Class<?> throwable : executable.getExceptionTypes()) {
			@SuppressWarnings("unchecked")
			Class<? extends Throwable> thr = (Class<? extends Throwable>) throwable;
			b.addThrownType(thr);
		}
		
		for (Parameter param : executable.getParameters()) {
			b.addParameter(param.getType(), param.getName(), param.isVarArgs());
		}
		
		return b;
	}
	
	private static String getConstructorBody(Constructor<?> c) {
		StringBuilder result = new StringBuilder().append("super(");
		Parameter[] params = c.getParameters();
		
		if (params.length > 0) {
			result.append(params[0].getName());
			
			for (int i = 1; i < params.length; i++) {
				result.append(", ").append(params[i].getName());
			}
		}
		
		return result.append(");").toString();
	}
	
	private static String getMethodBody(Method m) {
		Class<?> returnType = m.getReturnType();
		if (returnType == void.class) {
			return "";
		}
		
		return "return" +
					((returnType == boolean.class) ?
						" false" :
					 (returnType.isPrimitive() ?
						" 0" : " null"))
				+ ";";
	}
}
