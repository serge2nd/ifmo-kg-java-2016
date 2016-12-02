package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public abstract class AbstractVariable implements VariableElement {
	
	private final TypeMirror type;
	private final Name name;
	
	public AbstractVariable(TypeMirror type, String nameStr) {
		this.type = Objects.requireNonNull(type, "type");
		
		if (!SourceVersion.isIdentifier(nameStr) || SourceVersion.isKeyword(nameStr)) {
			throw new IllegalArgumentException("Incorrect variable name: " + nameStr);
		}

		this.name = new NameImpl(nameStr);
	}

	@Override
	public TypeMirror asType() { return type; }
	
	@Override
	public Name getSimpleName() { return name; }

	@Override
	public List<? extends Element> getEnclosedElements() {
		return Collections.emptyList();
	}

	@Override
	public Set<Modifier> getModifiers() {
		return Collections.emptySet();
	}
	
	@Override
	public Object getConstantValue() { return null; }
}
