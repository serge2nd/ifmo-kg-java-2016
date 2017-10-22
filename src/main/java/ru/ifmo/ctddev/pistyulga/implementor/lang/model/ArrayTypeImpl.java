package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

import ru.ifmo.ctddev.pistyulga.common.lang.model.AbstractType;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.util.TypeUtil;

public final class ArrayTypeImpl extends AbstractType implements ArrayType {
	
	private final String friendlyName;
	private final TypeMirror componentType;
	
	public ArrayTypeImpl(String name) {
		super(TypeKind.ARRAY, name);

		String friendlyName = super.toString(),
				componentName = friendlyName.substring(0, friendlyName.indexOf('['));
		
		this.friendlyName = friendlyName;
		this.componentType = TypeUtil.getType(ClassUtil.getTypeKind(componentName), componentName);
	}
	
	@Override
	public TypeMirror getComponentType() { return componentType; }
	
	@Override
	public String toString() { return friendlyName; }
	
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
