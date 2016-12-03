package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import javax.lang.model.element.ExecutableElement;

import ru.ifmo.ctddev.pistyulga.implementor.lang.builder.AbstractExecutableBuilder;

public final class ConstructorBuilder extends AbstractExecutableBuilder {
	
	private final int mods;
	
	public ConstructorBuilder(int mods) {
		this.mods = mods;
	}
	
	@Override
	public ExecutableElement build() {
		return new SimpleConstructorDescriptor(mods, annotations, parameters, thrownTypes, isVarArgs, body);
	}

}
