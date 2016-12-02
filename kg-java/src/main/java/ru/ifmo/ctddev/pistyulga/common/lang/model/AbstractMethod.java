package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ModifierUtil;

public abstract class AbstractMethod extends AbstractExecutable {
	
	private final Name name;
	private final Set<Modifier> modifiers;
	private final TypeMirror returnType;

	public AbstractMethod(String nameStr, int mods, TypeMirror returnType, List<AnnotationMirror> annotations,
			List<VariableElement> parameters, List<DeclaredType> thrownTypes, boolean isVarArgs)
	{
		super(annotations, parameters, thrownTypes, isVarArgs);
		
		if (!SourceVersion.isIdentifier(nameStr) || SourceVersion.isKeyword(nameStr)) {
			throw new IllegalArgumentException("Invalid method name: " + nameStr);
		}
		if (!ModifierUtil.isMethodMods(mods)) {
			throw new IllegalArgumentException("Not method modifiers: " + Integer.toHexString(mods));
		}

		TypeKind returnTypeKind = returnType.getKind();
		if (!returnTypeKind.isPrimitive() && returnTypeKind != TypeKind.ARRAY &&
			returnTypeKind != TypeKind.DECLARED && returnTypeKind != TypeKind.VOID)
		{
			throw new IllegalArgumentException("Cannot be returned: " + returnTypeKind);
		}
		
		this.name = new NameImpl(nameStr);
		this.modifiers = Collections.unmodifiableSet(ModifierUtil.getModifiers(mods));
		this.returnType = returnType;
	}
	
	@Override
	public ElementKind getKind() { return ElementKind.METHOD; }
	
	@Override
	public Name getSimpleName() { return name; }
	
	@Override
	public TypeMirror getReturnType() { return returnType; }
	
	@Override
	public Set<Modifier> getModifiers() { return modifiers; }
}
