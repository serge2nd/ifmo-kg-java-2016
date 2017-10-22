package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.model.AbstractConstructor;

final class SimpleConstructorDescriptor extends AbstractConstructor {
	
	private TypeElement declaringClass;

	public SimpleConstructorDescriptor(int mods, List<AnnotationMirror> annotations,
			List<VariableElement> parameters, List<DeclaredType> thrownTypes, boolean isVarArgs, String body)
	{
		super(mods, annotations, parameters, thrownTypes, isVarArgs, body);
	}
	
	@Override
	public Element getEnclosingElement() { return declaringClass; }
	
	void setDeclaringClass(TypeElement declaringClass) { this.declaringClass = declaringClass; }
	
	// **********************
	// *** Unused methods ***
	// **********************
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> arg0) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public <A extends Annotation> A[] getAnnotationsByType(Class<A> arg0) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public TypeMirror getReceiverType() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public List<? extends TypeParameterElement> getTypeParameters() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public TypeMirror asType() {
		throw new UnsupportedOperationException();
	}
}
