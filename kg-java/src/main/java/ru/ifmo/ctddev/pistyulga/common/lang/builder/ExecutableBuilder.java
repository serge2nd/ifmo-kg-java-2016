package ru.ifmo.ctddev.pistyulga.common.lang.builder;

import java.lang.annotation.Annotation;

import javax.lang.model.element.ExecutableElement;

import ru.ifmo.ctddev.pistyulga.common.builder.Builder;

public interface ExecutableBuilder extends Builder<ExecutableElement> {
	ExecutableBuilder addAnnotation(Class<? extends Annotation> annClass);
	ExecutableBuilder addParameter(Class<?> paramClass, String name);
	ExecutableBuilder addParameter(Class<?> paramClass, String name, boolean isVarArgs);
	ExecutableBuilder addThrownType(Class<? extends Throwable> throwable);
}
