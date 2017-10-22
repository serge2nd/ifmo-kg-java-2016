package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public abstract class AbstractExecutable implements ExecutableElement {
	
	private final List<AnnotationMirror> annotations;
	private final List<VariableElement> parameters;
	private final List<DeclaredType> thrownTypes;
	private final boolean isVarArgs;
	private final String body;
	
	public AbstractExecutable(List<AnnotationMirror> annotations, List<VariableElement> parameters,
			List<DeclaredType> thrownTypes, boolean isVarArgs, String body)
	{
		this.annotations = Collections.unmodifiableList(annotations);
		this.parameters = Collections.unmodifiableList(parameters);
		this.thrownTypes = Collections.unmodifiableList(thrownTypes);
		this.isVarArgs = isVarArgs;
		this.body = Objects.requireNonNull(body, "body");
	}
	
	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() { return annotations; }

	@Override
	public ElementKind getKind() { return ElementKind.CONSTRUCTOR; }
	
	@Override
	public Name getSimpleName() { return NameImpl.CONSTRUCTOR; }
	
	@Override
	public TypeMirror getReturnType() { return NoTypeImpl.VOID; }

	@Override
	public List<? extends VariableElement> getParameters() { return parameters; }
	
	@Override
	public List<? extends Element> getEnclosedElements() { return parameters; }

	@Override
	public List<? extends TypeMirror> getThrownTypes() { return thrownTypes; }
	
	@Override
	public boolean isVarArgs() { return isVarArgs; }
	
	@Override
	public boolean isDefault() { return false; }
	
	@Override
	public AnnotationValue getDefaultValue() { return null; }
	
	@Override
	public String toString() { return body; }
}
