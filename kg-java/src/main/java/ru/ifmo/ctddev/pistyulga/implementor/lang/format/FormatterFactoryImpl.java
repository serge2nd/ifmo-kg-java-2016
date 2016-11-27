package ru.ifmo.ctddev.pistyulga.implementor.lang.format;

import java.io.IOException;
import java.util.EnumMap;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import ru.ifmo.ctddev.pistyulga.common.format.LinePad;
import ru.ifmo.ctddev.pistyulga.common.lang.format.ElementFormatter;
import ru.ifmo.ctddev.pistyulga.common.lang.format.FormatterFactory;

public class FormatterFactoryImpl implements FormatterFactory<FormatKeyImpl> {

	@Override
	public ElementFormatter<?> getFormatter(FormatKeyImpl type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final ElementFormatter<FormatKeyImpl> classFormatter = new ElementFormatter<FormatKeyImpl>() {
		private LinePad pad;
		private void set(LinePad pad) { this.pad = pad; }
		
		private Appendable writeNewLine(Appendable dest) throws IOException {
			return dest.append('\n').append(pad);
		}

		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static final ElementFormatter<FormatKeyImpl> executableFormatter = new ElementFormatter<FormatKeyImpl>() {

		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static final ElementFormatter<FormatKeyImpl> variableFormatter = new ElementFormatter<FormatKeyImpl>() {

		@Override
		public void format(Element e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static void addParameters(ExecutableElement e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) {
		
	}
	
	private static void addThrownTypes(ExecutableElement e, Appendable dest, EnumMap<FormatKeyImpl, Object> params) {
		
	}
}
