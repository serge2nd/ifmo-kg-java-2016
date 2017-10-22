package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.model.AbstractTypeElement;

final class SimpleClassDescriptor extends AbstractTypeElement {
	
	private final List<DeclaredType> interfaces;
	private final List<Element> enclosedElements;

	public SimpleClassDescriptor(String nameStr, int mods, DeclaredType superClass,
			List<DeclaredType> interfaces, List<Element> enclosedElements)
	{
		super(nameStr, mods, superClass);
		
		this.interfaces = Collections.unmodifiableList(interfaces);
		this.enclosedElements = Collections.unmodifiableList(enclosedElements);
	}
	
	@Override
	public List<? extends TypeMirror> getInterfaces() { return interfaces; }
	
	@Override
	public List<? extends Element> getEnclosedElements() { return enclosedElements; }
	
	// **********************
	// *** Unused methods ***
	// **********************
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
	public <A extends Annotation> A getAnnotation(Class<A> arg0) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public <R, P> R accept(ElementVisitor<R, P> arg0, P arg1) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Element getEnclosingElement() {
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
	public TypeMirror asType() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
		throw new UnsupportedOperationException();
	}
}
