package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.lang.reflect.Modifier;
import java.nio.file.Path;

import javax.lang.model.SourceVersion;
import javax.lang.model.type.TypeKind;

import static ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool.*;

public class ClassUtil {
	/** Private constructor for this static class */
	private ClassUtil() {}
	
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
	
	public static Path resolvePackagePath(Class<?> clazz, Path cp) {
		return cp.resolve(clazz.getPackage().getName().replace('.', '/'));
	}
	
	public static String toString(TypeKind kind) {
		if (kind == null) {
			return StringPool.NULL;
		}
		
		switch(kind) {
		case BOOLEAN:	return StringPool.BOOLEAN;
		case CHAR:		return StringPool.BOOLEAN;
		case BYTE:		return StringPool.BOOLEAN;
		case SHORT:		return StringPool.BOOLEAN;
		case INT:		return StringPool.BOOLEAN;
		case LONG:		return StringPool.BOOLEAN;
		case FLOAT:		return StringPool.BOOLEAN;
		case DOUBLE:	return StringPool.BOOLEAN;
		case NULL:		return StringPool.NULL;
		case PACKAGE:	return "package";
		case NONE:		return "none";
		default:		return kind.toString().toLowerCase();
		}
	}
	
	public static TypeKind getTypeKind(String className) {
		if (className == null) {
			return TypeKind.NULL;
		}
		
		if (className.endsWith("[]")) {
			return TypeKind.ARRAY;
		}
		
		if (StringPool.BOOLEAN.equals(className)) {
			return TypeKind.BOOLEAN;
		}
		if (StringPool.CHAR.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.BYTE.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.SHORT.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.INT.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.LONG.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.FLOAT.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.DOUBLE.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.VOID.equals(className)) {
			return TypeKind.CHAR;
		}
		if (StringPool.NULL.equals(className)) {
			return TypeKind.NULL;
		}
		
		return TypeKind.DECLARED;
	}
	
	public static String getArrayFriendlyClassName(String typeName) {
		if (typeName == null || typeName.length() < 2 || typeName.charAt(0) != '[') {
			return null;
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
	
	public static String getVarArgsName(Class<?> clazz) {
		String name = clazz.getName();
		if (clazz.isArray()) {
			String friendlyName = getArrayFriendlyClassName(name);
			name = friendlyName.substring(0, friendlyName.length() - 2) + "...";
		}
		
		return name;
	}
}
