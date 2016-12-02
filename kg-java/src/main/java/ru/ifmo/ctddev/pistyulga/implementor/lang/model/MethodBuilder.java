package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import java.util.Objects;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;
import ru.ifmo.ctddev.pistyulga.implementor.lang.builder.AbstractExecutableBuilder;
import ru.ifmo.ctddev.pistyulga.implementor.lang.util.TypeUtil;

public final class MethodBuilder extends AbstractExecutableBuilder {
	
	private final int mods;
	private final String nameStr;
	private final TypeMirror returnType;
	
	public MethodBuilder(String nameStr, Class<?> returnType, int mods) {
		this.nameStr = Objects.requireNonNull(nameStr);
		
		String returnTypeName = returnType.getName();
		this.returnType = TypeUtil.getType(ClassUtil.getTypeKind(returnTypeName), returnTypeName);
		
		this.mods = mods;
	}
	
	@Override
	public ExecutableElement build() {
		return new SimpleMethodDescriptor(
				nameStr, mods, returnType, annotations, parameters, thrownTypes, isVarArgs);
	}

}
