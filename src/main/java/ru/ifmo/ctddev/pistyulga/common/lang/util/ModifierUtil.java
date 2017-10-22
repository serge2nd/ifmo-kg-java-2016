package ru.ifmo.ctddev.pistyulga.common.lang.util;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import static java.lang.reflect.Modifier.*;

public class ModifierUtil {
	/** Private constructor for this static class */
	private ModifierUtil() {}
	
	private static final EnumMap<ModifierEnum, Modifier> modifiersMap = new EnumMap<>(ModifierEnum.class);
	static {
		modifiersMap.put(ModifierEnum.PUBLIC, Modifier.PUBLIC);
		modifiersMap.put(ModifierEnum.PROTECTED, Modifier.PROTECTED);
		modifiersMap.put(ModifierEnum.PRIVATE, Modifier.PRIVATE);
		modifiersMap.put(ModifierEnum.ABSTRACT, Modifier.ABSTRACT);
		modifiersMap.put(ModifierEnum.STATIC, Modifier.STATIC);
		modifiersMap.put(ModifierEnum.FINAL, Modifier.FINAL);
		modifiersMap.put(ModifierEnum.TRANSIENT, Modifier.TRANSIENT);
		modifiersMap.put(ModifierEnum.VOLATILE, Modifier.VOLATILE);
		modifiersMap.put(ModifierEnum.SYNCHRONIZED, Modifier.SYNCHRONIZED);
		modifiersMap.put(ModifierEnum.NATIVE, Modifier.NATIVE);
		modifiersMap.put(ModifierEnum.STRICT, Modifier.STRICTFP);
	}
	
	public static Set<Modifier> getModifiers(int mods) {
		Set<Modifier> result = EnumSet.noneOf(Modifier.class);
		
		for (ModifierEnum enumMod : modifiersMap.keySet()) {
			if ((enumMod.val() & mods) > 0) {
				result.add(modifiersMap.get(enumMod));
			}
		}
		
		return result;
	}
	
	public static boolean isClassMods(int mods) {
		return (mods | classModifiers()) == classModifiers();
	}
	
	public static boolean isInterfaceMods(int mods) {
		return (mods | interfaceModifiers()) == interfaceModifiers();
	}
	
	public static boolean isConstructorMods(int mods) {
		return (mods | constructorModifiers()) == constructorModifiers();
	}
	
	public static boolean isMethodMods(int mods) {
		return (mods | methodModifiers()) == methodModifiers();
	}
}
