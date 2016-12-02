package ru.ifmo.ctddev.pistyulga.implementor.lang.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import ru.ifmo.ctddev.pistyulga.common.lang.model.MinimalDeclaredType;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.SimpleParameterDescriptor;
import ru.ifmo.ctddev.pistyulga.implementor.lang.util.TypeUtil;

public abstract class AbstractExecutableBuilder implements ExecutableBuilder {
	
	protected final List<AnnotationMirror> annotations = new ArrayList<>();
	protected final List<VariableElement> parameters = new ArrayList<>();
	protected final List<DeclaredType> thrownTypes = new ArrayList<>();
	protected boolean isVarArgs = false;
	
	@Override
	public ExecutableBuilder addAnnotation(Class<? extends Annotation> annClass) {
		if (!annClass.isAnnotation()) {
			throw new IllegalArgumentException("Not an annotation: " + annClass);
		}
		if (annClass.getDeclaredMethods().length > 0) {
			throw new UnsupportedOperationException("Annotations with parameters not supported yet");
		}
		
		annotations.add(new AnnotationMirror() {
			private final DeclaredType annotationType =
					MinimalDeclaredType.newInstance(annClass.getName());
			@Override
			public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValues() {
				return Collections.emptyMap();
			}
			@Override
			public DeclaredType getAnnotationType() { return annotationType; }
			@Override
			public String toString() { return annotationType.toString(); }
		});
		
		return this;
	}
	
	@Override
	public ExecutableBuilder addParameter(Class<?> paramClass, String name) {
		return this.addParameter(paramClass, name, false);
	}
	
	@Override
	public ExecutableBuilder addParameter(Class<?> paramClass, String name, boolean isVarArgs) {
		if (this.isVarArgs) {
			throw new IllegalStateException("A varargs parameter must be last");
		}
		if (isVarArgs && !paramClass.isArray()) {
			throw new IllegalArgumentException("A varargs parameter must be an array");
		}
		
		this.isVarArgs = isVarArgs;
		String className = paramClass.getName();
		
		parameters.add(new SimpleParameterDescriptor(
				TypeUtil.getType(ClassUtil.getTypeKind(className), className), name));
		
		return this;
	}
	
	@Override
	public ExecutableBuilder addThrownType(Class<? extends Throwable> throwable) {
		thrownTypes.add(MinimalDeclaredType.newInstance(throwable.getName()));
		return this;
	}
}
