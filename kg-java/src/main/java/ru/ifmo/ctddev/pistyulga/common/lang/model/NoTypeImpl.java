package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.common.lang.util.StringPool;

public final class NoTypeImpl extends AbstractType implements NoType {
	public static final NoType
		VOID = new NoTypeImpl(TypeKind.VOID, null),
		PACKAGE = new NoTypeImpl(TypeKind.PACKAGE, ClassUtil.toString(TypeKind.PACKAGE)),
		NONE = new NoTypeImpl(TypeKind.NONE, null),
		
		JAVA_LANG = new NoTypeImpl(TypeKind.PACKAGE, StringPool.JAVA_LANG);
	
	/** Private constructor for this fixed object pool */
	private NoTypeImpl(TypeKind kind, String name) { super(kind, name); }
	
	public static NoType getInstance(TypeKind kind) {
		if (kind == TypeKind.VOID) {
			return VOID;
		}
		if (kind == TypeKind.NONE) {
			return NONE;
		}
		if (kind == TypeKind.PACKAGE) {
			return PACKAGE;
		}
		
		throw new IllegalArgumentException("Invalid kind of NoType: " + kind);
	}
	
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
