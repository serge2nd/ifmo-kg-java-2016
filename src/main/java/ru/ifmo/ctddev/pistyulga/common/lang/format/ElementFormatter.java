package ru.ifmo.ctddev.pistyulga.common.lang.format;

import java.io.IOException;
import java.util.EnumMap;

import javax.lang.model.element.Element;

public interface ElementFormatter<E extends Enum<E> & FormatKey<E>> {
	void format(Element e, Appendable dest, EnumMap<E, Object> params) throws IOException;
}
