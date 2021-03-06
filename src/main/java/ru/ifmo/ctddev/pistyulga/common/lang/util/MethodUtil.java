package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Utility methods working with methods
 */
public class MethodUtil {
	/** Private constructor for this static class */
	private MethodUtil() {}
	
	/**
	 * Gets method signature (see what returns)
	 * @param method - a method
	 * @return - a string formatted like "{@code <method_name>(<param1_type>, <param2_type>, ..., <paramN_type>)}"
	 */
	public static String getSignature(Method method) {
		StringBuilder result =
				new StringBuilder(method.getName()).append('(');
		
		Parameter[] params = method.getParameters();
		int count = params.length;
		
		if (count > 0) {
			Parameter param = params[0];
			String typeName = param.getType().getName();
			result.append((count == 1 && param.isVarArgs()) ?
							ClassUtil.getVarArgName(typeName) :
							ClassUtil.getFriendlyName(typeName));
			
			int i;
			for (i = 1; i < count - 1; i++) {
				typeName = params[i].getType().getName();
				result.append(", ").append(ClassUtil.getFriendlyName(typeName));
			}
			
			if (count > 1) {
				param = params[i];
				typeName = param.getType().getName();
				result.append(", ").append(param.isVarArgs() ?
								ClassUtil.getVarArgName(typeName) :
								ClassUtil.getFriendlyName(typeName));
			}
		}
		
		return result.append(')').toString();
	}
}
