package ru.ifmo.ctddev.pistyulga.implementor.lang.format;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import ru.ifmo.ctddev.pistyulga.common.format.LinePad;
import ru.ifmo.ctddev.pistyulga.common.lang.format.ElementFormatter;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatterFactory;
import ru.ifmo.ctddev.pistyulga.common.lang.model.MinimalDeclaredType;
import ru.ifmo.ctddev.pistyulga.common.lang.model.NoTypeImpl;
import ru.ifmo.ctddev.pistyulga.common.lang.util.ClassUtil;

import static ru.ifmo.ctddev.pistyulga.implementor.lang.format.FormatKeyImpl.*;

public class FormatterFactoryImpl implements FormatterFactory<FormatKeyImpl> {
	
	private static final FormatterFactory<FormatKeyImpl> instance = new FormatterFactoryImpl();
	
	/** Private constructor for this singleton */
	private FormatterFactoryImpl() {}
	
	public static FormatterFactory<FormatKeyImpl> getInstance() { return instance; }

	@Override
	public ElementFormatter<?> getFormatter(FormatKeyImpl type) {
		switch(type) {
		case CLASS:		return classFormatter;
		case METHOD:	return executableFormatter;
		case IMPORTS:	return importsFormatter;
		default:
		}
		
		return new ElementFormatter<FormatKeyImpl>() {
			@Override
			public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) throws IOException {
				dest.append(e.toString());
			}
		};
	}
	
	private static final ElementFormatter<FormatKeyImpl> importsFormatter = new ElementFormatter<FormatKeyImpl>() {

		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) throws IOException {
			String typeElementName = null;
			if (e instanceof TypeElement)
				typeElementName = ((TypeElement)e).getQualifiedName().toString();
			
			SortedSet<String> imports = new TreeSet<>();
			recursiveFormat(e, params, imports);
			
			for (String importName : imports) {
				if (!ClassUtil.isSameLocation(typeElementName, importName))
					dest.append("import ").append(importName).append(";\n");
			}
		}
		
		private void recursiveFormat(Element e, EnumMap<FormatKeyImpl, Object> params, SortedSet<String> imports)
				throws IOException
		{	
			if (e instanceof TypeElement) {
				TypeElement typeElement = (TypeElement) e;
				TypeMirror superType = typeElement.getSuperclass();
				resolveType(superType, imports);
				
				writeInterfaces(typeElement, fakeAppendable, imports);
				
				for (Element enclosedElement : typeElement.getEnclosedElements()) {
					recursiveFormat(enclosedElement, params, imports);
				}
			} else if (e instanceof ExecutableElement) {
				ExecutableElement executableElement = (ExecutableElement) e;
				TypeMirror returnType = executableElement.getReturnType();
				resolveType(returnType, imports);
				
				writeAnnotations(executableElement, fakeAppendable, params, imports);
				writeThrownTypes(executableElement, fakeAppendable, params, imports);
				writeParameters(executableElement, fakeAppendable, imports);
			}
		}
	};
	
	private static TypeMirror resolveType(TypeMirror type, SortedSet<String> imports)
			throws IOException
	{
		if (imports == null || !(type instanceof DeclaredType)) {
			return type;
		}
		
		DeclaredType declaredType = (DeclaredType) type;
		if (declaredType.getEnclosingType() != NoTypeImpl.JAVA_LANG) {
			String fullName = type.toString();
			if (!imports.contains(fullName)) {
				imports.add(fullName);
			}
		}
		
		return type;
	}
	
	private static final ElementFormatter<FormatKeyImpl> classFormatter = new ElementFormatter<FormatKeyImpl>() {
		private LinePad pad;
		private void set(LinePad pad) { this.pad = pad; }
		
		private Appendable writeNewLine(Appendable dest) throws IOException {
			return dest.append('\n').append(pad);
		}

		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) throws IOException {
			// Type check
			TypeElement typeElement = (TypeElement) e;
			ElementKind kind = typeElement.getKind();
			if (kind != ElementKind.CLASS) {
				throw new UnsupportedOperationException("Must be a class, given " + kind);
			}
			
			// Imports
			importsFormatter.format(typeElement, dest, params);
			
			// Set padding & write modifiers & name
			set(PAD.from(params)); PAD.to(params, LinePad.T1);
			writeNewLine(dest);
			writeModifiers(typeElement, dest)
				.append("class ")
				.append(typeElement.getSimpleName());
			
			// Superclass
			TypeMirror superType = typeElement.getSuperclass();
			if (superType != null && superType != MinimalDeclaredType.OBJECT) {
				String typeName = resolveType(superType, null).toString();
				dest.append(" extends ").append(ClassUtil.getSimpleName(typeName));
			}
			
			// Interfaces
			writeInterfaces(typeElement, dest, null)
				.append(" {");
			
			// Enclosed elements
			writeNewLine(dest);
			for (Element enclosedElement : typeElement.getEnclosedElements()) {
				executableFormatter.format(enclosedElement, dest, params);
			}
			
			dest.append("}\n");
		}
		
		
	};
	
	private static final ElementFormatter<FormatKeyImpl> executableFormatter = new ElementFormatter<FormatKeyImpl>() {
		private LinePad pad;
		private void set(LinePad pad) { this.pad = pad; }
		
		private Appendable writeNewLine(Appendable dest) throws IOException {
			return dest.append('\n').append(pad);
		}
		
		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) throws IOException {
			// Type check
			ExecutableElement executableElement = (ExecutableElement) e;
			ElementKind kind = executableElement.getKind();
			if (kind != ElementKind.CONSTRUCTOR && kind != ElementKind.METHOD)
			{
				throw new UnsupportedOperationException("Must be a method or a constructor, given " + kind);
			}
			
			String returnTypeName = resolveType(executableElement.getReturnType(), null).toString();
			
			// Set padding & write modifiers
			set(PAD.from(params));
			writeNewLine(dest);
			writeAnnotations(executableElement, dest, params, null);
			writeModifiers(executableElement, dest);
			
			// Return type & name
			if (kind == ElementKind.CONSTRUCTOR) {
				dest.append(executableElement.getEnclosingElement().getSimpleName());
			} else {
				dest.append(ClassUtil.getSimpleName(returnTypeName));
				dest.append(' ').append(executableElement.getSimpleName());
			}
			
			// Parameters & thrown types
			writeParameters(executableElement, dest.append('('), null)
				.append(')');
			writeThrownTypes(executableElement, dest, params, null);
			
			// Body
			writeNewLine(dest).append('{').append(BODY.from(params)).append("}\n");
		}
	};
	
	private static Appendable writeModifiers(Element e, Appendable dest) throws IOException {
		for (Modifier mod : e.getModifiers()) {
			dest.append(mod.toString()).append(' ');
		}
		
		return dest;
	}
	
	private static Appendable writeInterfaces(TypeElement e, Appendable dest, SortedSet<String> imports)
			throws IOException
	{
		@SuppressWarnings("unchecked")
		List<DeclaredType> interfaces = (List<DeclaredType>) e.getInterfaces();
		int count = interfaces.size();
		
		if (count > 0) {
			dest.append(" implements ");
			Iterator<? extends TypeMirror> it = interfaces.iterator();
			String name = resolveType(it.next(), imports).toString();
			dest.append(ClassUtil.getSimpleName(name));
			
			while (it.hasNext()) {
				name = resolveType(it.next(), imports).toString();
				dest.append(", ").append(ClassUtil.getSimpleName(name));
			}
		}
		
		return dest;
	}
	
	private static Appendable writeAnnotations(AnnotatedConstruct e, Appendable dest,
			EnumMap<FormatKeyImpl, Object> params, SortedSet<String> imports)
			throws IOException
	{
		List<? extends AnnotationMirror> annotation = e.getAnnotationMirrors();
		CharSequence pad = PAD.from(params);
		
		for(AnnotationMirror ann : annotation) {
			String name = resolveType(ann.getAnnotationType(), imports).toString();
			dest.append('@').append(ClassUtil.getSimpleName(name));
			dest.append('\n').append(pad);
		}
		
		return dest;
	}
	
	private static Appendable writeParameters(ExecutableElement e, Appendable dest, SortedSet<String> imports)
			throws IOException
	{
		List<? extends VariableElement> elementParams = e.getParameters();
		int count = elementParams.size();
		
		if (count > 0) {
			Iterator<? extends VariableElement> it = elementParams.iterator();
			writeParameter(it.next(), dest, imports,
					(count == 1) ? e.isVarArgs() : false);
			
			for (int i = 1; i < count - 1; i++) {
				dest.append(", ");
				writeParameter(it.next(), dest, imports, false);
			}
			
			if (count > 1) {
				dest.append(", ");
				writeParameter(it.next(), dest, imports, e.isVarArgs());
			}
		}
		
		return dest;
	}
	
	private static Appendable writeParameter(VariableElement p,
			Appendable dest, SortedSet<String> imports, boolean isVarArgs)
			throws IOException
	{
		TypeMirror type = p.asType();
		String typeStr = ClassUtil.getSimpleName(type.toString());
		
		if (isVarArgs) {
			type = ((ArrayType)type).getComponentType();
			typeStr = ClassUtil.getVarArgName(typeStr);
		}
		
		resolveType(type, imports);
		dest.append(typeStr).append(' ').append(p.getSimpleName());
		
		return dest;
	}
	
	private static Appendable writeThrownTypes(ExecutableElement e, Appendable dest,
			EnumMap<FormatKeyImpl, Object> params, SortedSet<String> imports)
			throws IOException
	{
		@SuppressWarnings("unchecked")
		List<DeclaredType> thrownTypes = (List<DeclaredType>) e.getThrownTypes();
		int count = thrownTypes.size();
		
		if (count > 0) {
			dest.append('\n').append(PAD.from(params));
			dest.append('\t').append("throws ");
			
			Iterator<DeclaredType> it = thrownTypes.iterator();
			String name = resolveType(it.next(), imports).toString();
			dest.append(ClassUtil.getSimpleName(name));
			
			while (it.hasNext()) {
				name = resolveType(it.next(), imports).toString();
				dest.append(", ").append(ClassUtil.getSimpleName(name));
			}
		}
		
		return dest;
	}
	
	private static Appendable fakeAppendable = new Appendable() {
		@Override
		public Appendable append(CharSequence csq, int start, int end) throws IOException { return this; }
		@Override
		public Appendable append(char c) throws IOException { return this; }
		@Override
		public Appendable append(CharSequence csq) throws IOException { return this; }
	};
}
