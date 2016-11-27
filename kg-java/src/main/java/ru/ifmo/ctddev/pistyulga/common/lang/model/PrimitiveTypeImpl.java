package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVisitor;

public final class PrimitiveTypeImpl extends AbstractType implements PrimitiveType {
	
	public static final PrimitiveType
		BOOLEAN = new PrimitiveTypeImpl(TypeKind.BOOLEAN),
		CHAR 	= new PrimitiveTypeImpl(TypeKind.CHAR),
		BYTE 	= new PrimitiveTypeImpl(TypeKind.BYTE),
		SHORT 	= new PrimitiveTypeImpl(TypeKind.SHORT),
		INT 	= new PrimitiveTypeImpl(TypeKind.INT),
		LONG 	= new PrimitiveTypeImpl(TypeKind.LONG),
		FLOAT 	= new PrimitiveTypeImpl(TypeKind.FLOAT),
		DOUBLE 	= new PrimitiveTypeImpl(TypeKind.DOUBLE);
		
	
	private PrimitiveTypeImpl(TypeKind kind) { super(kind, null); }
	
	public static PrimitiveType getInstance(TypeKind kind) {
		switch(kind) {
		case BOOLEAN:	return BOOLEAN;
		case CHAR:		return CHAR;
		case BYTE:		return BYTE;
		case SHORT:		return SHORT;
		case INT:		return INT;
		case LONG:		return LONG;
		case FLOAT:		return FLOAT;
		case DOUBLE:	return DOUBLE;
		default:		throw new IllegalArgumentException("Not a primitive: " + kind);
		}
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
