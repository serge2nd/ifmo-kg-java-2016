package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

public final class NoTypeImpl extends AbstractType implements NoType {
	public static final NoType
		VOID = new NoTypeImpl(TypeKind.VOID, null),
		JAVA_LANG = new NoTypeImpl(TypeKind.PACKAGE, "java.lang"),
		NONE = new NoTypeImpl(TypeKind.NONE, null);
	
	private TypeKind kind;
	private NoTypeImpl(TypeKind kind, String name) { super(kind, name); }
	
	public static NoType getInstance(TypeKind kind) {
		if (kind == TypeKind.VOID) {
			return VOID;
		}
		if (kind == TypeKind.NONE) {
			return NONE;
		}
		if (kind == TypeKind.PACKAGE) {
			return JAVA_LANG;
		}
		
		throw new IllegalArgumentException();
	}
	
	public static NoType getInstance(String name) {
		if (JAVA_LANG.toString().equals(name)) {
			return JAVA_LANG;
		}
		
		return new NoTypeImpl(TypeKind.PACKAGE, name);
	}
	
	@Override
	public TypeKind getKind() { return kind; }
	
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
}
