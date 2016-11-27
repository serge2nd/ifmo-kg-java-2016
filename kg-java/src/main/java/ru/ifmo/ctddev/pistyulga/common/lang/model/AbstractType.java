package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Objects;

import javax.lang.model.SourceVersion;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;

public abstract class AbstractType implements TypeMirror {
	
	private final TypeKind kind;
	private final String name;
	
	public AbstractType(TypeKind kind, String name) {
		this.kind = Objects.requireNonNull(kind, "kind");
		
		if (kind == TypeKind.VOID ||
			kind == TypeKind.NULL ||
			kind == TypeKind.NONE ||
			kind.isPrimitive())
		{
			this.name = ClassUtil.toString(kind);
		} else if (kind == TypeKind.ARRAY) {
			String friendlyName = ClassUtil.getArrayFriendlyClassName(name);
			if (friendlyName == null) {
				throw new IllegalArgumentException("Incorrect array type name: " + name);
			}
			
			this.name = friendlyName;
		} else if (kind == TypeKind.DECLARED || kind == TypeKind.PACKAGE) {
			if (!SourceVersion.isName(name)) {
				throw new IllegalArgumentException("Incorrect name: " + name);
			}
			
			this.name = name;
		} else {
			throw new UnsupportedOperationException("This type not supported yet");
		}
	}

	@Override
	public TypeKind getKind() { return kind; }
	
	@Override
	public String toString() { return name; }
}
