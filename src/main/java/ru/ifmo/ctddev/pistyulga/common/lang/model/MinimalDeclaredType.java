package ru.ifmo.ctddev.pistyulga.common.lang.model;

import static ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool.NAME_SEPARATOR;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

import ru.ifmo.ctddev.pistyulga.common.lang.util.StringPool;

public final class MinimalDeclaredType extends AbstractType implements DeclaredType {
	
	private final TypeMirror enclosingType;
	
	/** Frequently used type */
	public final static DeclaredType
		OBJECT = new MinimalDeclaredType("java.lang.Object", NoTypeImpl.JAVA_LANG),
		STRING = new MinimalDeclaredType("java.lang.String", NoTypeImpl.JAVA_LANG);
	
	/**
	 * Private constructor because {@link #newInstance(String)} is used
	 * @param name
	 * @param enclosingType
	 */
	private MinimalDeclaredType(String name, TypeMirror enclosingType) {
		super(TypeKind.DECLARED, name);
		
		this.enclosingType = enclosingType;
	}
	
	public static DeclaredType newInstance(String fullName) {
		if (OBJECT.toString().equals(fullName)) {
			return OBJECT;
		}
		if (STRING.toString().equals(fullName)) {
			return STRING;
		}
		
		int lastDelimIndex = fullName.lastIndexOf(NAME_SEPARATOR);
		if (lastDelimIndex == 0 || lastDelimIndex == fullName.length() - 1) {
			throw new IllegalArgumentException("Incorrect class name: " + fullName);
		}
		
		if (lastDelimIndex < 0) {
			return new MinimalDeclaredType(fullName, NoTypeImpl.PACKAGE);
		}
		
		String enclosingName = fullName.substring(0, lastDelimIndex);
		
		return new MinimalDeclaredType(fullName, StringPool.JAVA_LANG.equals(enclosingName) ?
				NoTypeImpl.JAVA_LANG : NoTypeImpl.PACKAGE);
	}
	
	@Override
	public TypeMirror getEnclosingType() { return enclosingType; }
	
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
