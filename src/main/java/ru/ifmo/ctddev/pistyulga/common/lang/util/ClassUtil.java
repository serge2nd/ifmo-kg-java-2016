package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.lang.reflect.Modifier;
import java.nio.file.Path;

import javax.lang.model.SourceVersion;
import javax.lang.model.type.TypeKind;

import static ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool.*;
import static java.io.File.separatorChar;

public class ClassUtil {
	/** Private constructor for this static class */
	private ClassUtil() {}
	
	/**
	 * @param clazz - a class
	 * @param cp - class path
	 * @return {@code null} if this class is {@link null}, enum or final;
	 * the given class path if the class is public; the given class path
	 * concatenated with the package path (e.g. for "${project}/src/main/java"
	 * and the package {@code java.lang} it will be "${project}/src/main/java/java/lang")
	 * otherwise
	 */
	public static Path getSubclassPath(Class<?> clazz, Path cp) {
		if (clazz == null || Enum.class.isAssignableFrom(clazz) ||
				Modifier.isFinal(clazz.getModifiers()))
		{
			return null;
		}
		
		if (Modifier.isPublic(clazz.getModifiers())) {
			return cp;
		}
		
		return resolvePackagePath(clazz, cp);
	}
	
	/**
	 * Adds package path to the given class path (e.g. for "${project}/src/main/java"
	 * and the package {@code java.lang} it will be "${project}/src/main/java/java/lang")
	 * @param clazz - a class
	 * @param cp - class path
	 * @return the result
	 */
	public static Path resolvePackagePath(Class<?> clazz, Path cp) {
		Package pkg = clazz.getPackage();
		if (pkg == null) {
			return cp;
		}
		return cp.resolve(pkg.getName().replace(NAME_SEPARATOR, separatorChar));
	}
	
	public static String toString(TypeKind kind) {
		if (kind == null) {
			return StringPool.NULL;
		}
		
		switch(kind) {
		case BOOLEAN:	return StringPool.BOOLEAN;
		case CHAR:		return StringPool.CHAR;
		case BYTE:		return StringPool.BYTE;
		case SHORT:		return StringPool.SHORT;
		case INT:		return StringPool.INT;
		case LONG:		return StringPool.LONG;
		case FLOAT:		return StringPool.FLOAT;
		case DOUBLE:	return StringPool.DOUBLE;
		case NULL:		return StringPool.NULL;
		case PACKAGE:	return StringPool.PACKAGE;
		case NONE:		return StringPool.NONE;
		default:		return kind.toString().toLowerCase();
		}
	}
	
	public static String getSimpleName(String fullName) {
		int lastDelimIndex = fullName.lastIndexOf(NAME_SEPARATOR);
		return (lastDelimIndex < 0) ? fullName :
				fullName.substring(lastDelimIndex + 1);
	}
	
	public static boolean isSameLocation(String fullName1, String fullName2) {
		if (fullName1 == null || fullName2 == null) {
			return false;
		}
		
		int nameStart1 = fullName1.lastIndexOf(NAME_SEPARATOR),
			nameStart2 = fullName2.lastIndexOf(NAME_SEPARATOR);
		
		if (nameStart1 != nameStart2) {
			return false;
		}
		if (nameStart1 < 0) {
			return true;
		}
		
		String packageName1 = fullName1.substring(0, nameStart1),
				packageName2 = fullName2.substring(0, nameStart2);
		
		return packageName1.equals(packageName2);
	}
	
	public static TypeKind getTypeKind(String className) {
		if (className == null) {
			return TypeKind.NULL;
		}
		
		if (className.endsWith("[]") || className.startsWith("[")) {
			return TypeKind.ARRAY;
		}
		
		if (StringPool.BOOLEAN.equals(className)) {
			return TypeKind.BOOLEAN;
		}
		if (StringPool.CHAR.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.BYTE.equals(className)) {
			return TypeKind.BYTE;
		}
		if (StringPool.SHORT.equals(className)) {
			return TypeKind.SHORT;
		}
		if (StringPool.INT.equals(className)) {
			return TypeKind.INT;
		}
		if (StringPool.LONG.equals(className)) {
			return TypeKind.LONG;
		}
		if (StringPool.FLOAT.equals(className)) {
			return TypeKind.FLOAT;
		}
		if (StringPool.DOUBLE.equals(className)) {
			return TypeKind.DOUBLE;
		}
		if (StringPool.VOID.equals(className)) {
			return TypeKind.VOID;
		}
		if (StringPool.NULL.equals(className)) {
			return TypeKind.NULL;
		}
		
		return TypeKind.DECLARED;
	}
	
	public static String getFriendlyName(String typeName) {
		if (typeName == null || typeName.length() < 2 || typeName.charAt(0) != '[') {
			return typeName;
		}
		
		int depth = 1, strLen = typeName.length();
		while (depth < strLen && typeName.charAt(depth) == '[') {
			depth++;
		}
		if (depth == strLen) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
		
		int typeIndex = depth;
		char type = typeName.charAt(typeIndex);

		switch(type) {
		case BOOL:		result.append(StringPool.BOOLEAN); break;
		case CHAR:		result.append(StringPool.CHAR); break;
		case BYTE:		result.append(StringPool.BYTE); break;
		case SHORT:		result.append(StringPool.SHORT); break;
		case INT:		result.append(StringPool.INT); break;
		case LONG:		result.append(StringPool.LONG); break;
		case FLOAT:		result.append(StringPool.FLOAT); break;
		case DOUBLE:	result.append(StringPool.DOUBLE); break;
		case CLASS:		break;
		default:		return null;
		}
		
		if (type == CLASS) {
			if (typeIndex + 1 == strLen || typeName.charAt(strLen - 1) != ';') {
				return null;
			}
			
			String componentName = typeName.substring(typeIndex + 1, strLen - 1);
			if (!SourceVersion.isName(componentName)) {
				return null;
			}
			
			result.append(componentName);
		}
		
		while (depth-- > 0) {
			result.append("[]");
		}
		
		return result.toString();
	}
	
	public static String getVarArgName(String typeName) {
		if (typeName == null || typeName.isEmpty()) {
			return typeName;
		}
		
		String oldTypeName = typeName;
		if (typeName.charAt(0) == '[') {
			typeName = getFriendlyName(typeName);
		}
		if (typeName == null) {
			return oldTypeName;
		}
		
		if (typeName.endsWith("[]")) {
			typeName = typeName.substring(0, typeName.length() - 2) + "...";
		}
		
		return typeName;
	}
}
