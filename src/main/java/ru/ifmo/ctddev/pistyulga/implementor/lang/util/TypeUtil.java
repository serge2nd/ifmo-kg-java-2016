package ru.ifmo.ctddev.pistyulga.implementor.lang.util;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.lang.model.MinimalDeclaredType;
import ru.ifmo.ctddev.pistyulga.common.lang.model.NoTypeImpl;
import ru.ifmo.ctddev.pistyulga.common.lang.model.NullTypeImpl;
import ru.ifmo.ctddev.pistyulga.common.lang.model.PrimitiveTypeImpl;
import ru.ifmo.ctddev.pistyulga.implementor.lang.model.ArrayTypeImpl;

public class TypeUtil {
	/** Private constructor for this static class */
	private TypeUtil() {}
	
	public static TypeMirror getType(TypeKind kind, String name) {
		if (kind == null) {
			return NullTypeImpl.getInstance();
		}
		
		if (kind.isPrimitive()) {
			return PrimitiveTypeImpl.getInstance(kind);
		}
		
		switch(kind) {
		case VOID:		return NoTypeImpl.VOID;
		case NONE:		return NoTypeImpl.NONE;
		case PACKAGE:	return NoTypeImpl.PACKAGE;
		case NULL:		return NullTypeImpl.getInstance();
		case ARRAY:		return new ArrayTypeImpl(name);
		case DECLARED:	return MinimalDeclaredType.newInstance(name);
		default:
		}
		
		throw new UnsupportedOperationException("This type not supported yet: " + kind);
	}
}
