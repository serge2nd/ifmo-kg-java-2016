package ru.ifmo.ctddev.pistyulga.implementor.lang.model;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import ru.ifmo.ctddev.pistyulga.common.builder.Builder;
import ru.ifmo.ctddev.pistyulga.common.lang.model.MinimalDeclaredType;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;

public final class ClassBuilder implements Builder<TypeElement> {
	
	private final String nameStr;
	private final int mods;
	private DeclaredType superClass;
	private final List<DeclaredType> interfaces = new ArrayList<>();
	private final List<Element> enclosedElements = new ArrayList<>();
	
	public ClassBuilder(String nameStr, int mods) {
		this.nameStr = Objects.requireNonNull(nameStr, "nameStr");
		this.mods = mods;
	}
	
	public ClassBuilder setSuperClass(Class<?> clazz) {
		if (ClassUtil.getSubclassPath(clazz, Paths.get(".")) == null) {
			throw new IllegalArgumentException("Cannot be a superclass: " + clazz.getName());
		}
		if (clazz.isInterface()) {
			throw new IllegalArgumentException("Must be a class: " + clazz.getName());
		}
		if (clazz != Object.class)
			this.superClass = MinimalDeclaredType.newInstance(clazz.getName());
		
		return this;
	}
	
	public ClassBuilder addInterface(Class<?> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Must be an interface: " + clazz.getName());
		}
		interfaces.add(MinimalDeclaredType.newInstance(clazz.getName()));
		
		return this;
	}
	
	public ClassBuilder addEnclosedElement(Element e) {
		enclosedElements.add(Objects.requireNonNull(e, "element"));
		return this;
	}

	@Override
	public TypeElement build() {
		TypeElement result =
				new SimpleClassDescriptor(nameStr, mods, superClass, interfaces, enclosedElements);
		
		for (Element enclosedElement : enclosedElements) {
			if (enclosedElement instanceof SimpleConstructorDescriptor) {
				((SimpleConstructorDescriptor)enclosedElement)
					.setDeclaringClass(result);
			}
		}
		
		return result;
	}
}
