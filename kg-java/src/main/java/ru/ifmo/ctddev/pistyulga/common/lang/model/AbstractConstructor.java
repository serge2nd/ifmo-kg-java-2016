package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ModifierUtil;

public abstract class AbstractConstructor extends AbstractExecutable {
	
	private final Set<Modifier> modifiers;

	public AbstractConstructor(int mods, List<AnnotationMirror> annotations, List<VariableElement> parameters,
			List<DeclaredType> thrownTypes, boolean isVarArgs, String body)
	{
		super(annotations, parameters, thrownTypes, isVarArgs, body);

		if (!ModifierUtil.isConstructorMods(mods)) {
			throw new IllegalArgumentException("Not constructor modifiers: " + Integer.toHexString(mods));
		}
		
		this.modifiers = Collections.unmodifiableSet(ModifierUtil.getModifiers(mods));
	}
	
	@Override
	public Set<Modifier> getModifiers() { return modifiers; }
}
