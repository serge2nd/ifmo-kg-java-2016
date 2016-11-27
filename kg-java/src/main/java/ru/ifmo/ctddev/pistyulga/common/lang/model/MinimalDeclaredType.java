package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

public final class MinimalDeclaredType extends AbstractType implements DeclaredType {
	
	private final TypeMirror enclosingType;
	
	public final static DeclaredType
		OBJECT = new MinimalDeclaredType("Object", NoTypeImpl.JAVA_LANG),
		STRING = new MinimalDeclaredType("String", NoTypeImpl.JAVA_LANG);
	
	private MinimalDeclaredType(String name, TypeMirror enclosingType) {
		super(TypeKind.DECLARED, name);
		if (enclosingType.getKind() != TypeKind.PACKAGE) {
			throw new UnsupportedOperationException("Only the package enclosing type is supported currently");
		}
		
		this.enclosingType = enclosingType;
	}
	
	public static DeclaredType getInstance(String name, TypeMirror enclosingType) {
		if (enclosingType.getKind() != TypeKind.PACKAGE) {
			throw new UnsupportedOperationException("Only the package enclosing type is supported currently");
		}
		
		if (OBJECT.toString().equals(name)) {
			return OBJECT;
		}
		if (STRING.toString().equals(name)) {
			return STRING;
		}
		
		return new MinimalDeclaredType(name, enclosingType);
	}
	
	@Override
	public TypeMirror getEnclosingType() { return enclosingType; }
	
	@Override
	public String toString() { return enclosingType.toString() + "." + super.toString(); }
	
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
	public List<? extends AnnotationMirror> getAnnotationMirrors() {
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
	public <R, P> R accept(TypeVisitor<R, P> v, P p) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Element asElement() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @throws UnsupportedOperationException
	 */
	@Override
	public List<? extends TypeMirror> getTypeArguments() {
		throw new UnsupportedOperationException();
	}
}
