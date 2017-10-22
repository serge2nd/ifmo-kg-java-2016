package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ModifierUtil;

public abstract class AbstractTypeElement implements TypeElement {
	
	private final Name name;
	private final Name simpleName;
	private final Set<Modifier> modifiers;
	private final DeclaredType superClass;
	private final boolean isInterface;
	
	private static boolean isInterface(int mods) {
		return java.lang.reflect.Modifier.isInterface(mods);
	}
	
	public AbstractTypeElement(String nameStr, int mods, DeclaredType superClass) {
		if (!SourceVersion.isName(nameStr)) {
			throw new IllegalArgumentException("Incorrect name: " + nameStr);
		}
		
		this.isInterface = isInterface(mods);
		
		if ((isInterface || !ModifierUtil.isClassMods(mods)) &&
			(!isInterface || !ModifierUtil.isInterfaceMods(mods)))
		{
			throw new IllegalArgumentException("Incorrect class or interface modifiers: " + Integer.toHexString(mods));
		}
		if ((isInterface || nameStr.equals(Object.class.getName()))
				&& superClass != null)
		{
			throw new IllegalArgumentException("An interface or Object cannot have superclass");
		} else {
			if (superClass == null) {
				superClass = MinimalDeclaredType.OBJECT;
			}
			if (superClass.getKind() != TypeKind.DECLARED) {
				throw new IllegalArgumentException("The parent must be a class");
			}
		}
		
		this.name = new NameImpl(nameStr);
		this.superClass = superClass;
		this.simpleName = new NameImpl(ClassUtil.getSimpleName(nameStr));
		this.modifiers = Collections.unmodifiableSet(ModifierUtil.getModifiers(mods));
	}
	
	@Override
	public ElementKind getKind() { return isInterface ? ElementKind.INTERFACE : ElementKind.CLASS; }

	@Override
	public Set<Modifier> getModifiers() { return modifiers; }
	
	@Override
	public TypeMirror getSuperclass() {
		if (superClass == null) {
			return NoTypeImpl.NONE;
		}
		
		return superClass;
	}
	
	@Override
	public Name getQualifiedName() { return name; }

	@Override
	public Name getSimpleName() { return simpleName; }
	
	@Override
	public NestingKind getNestingKind() { return NestingKind.TOP_LEVEL; }
}
