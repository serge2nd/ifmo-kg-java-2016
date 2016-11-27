package ru.ifmo.ctddev.pistyulga.common.lang.model;

import java.util.Objects;

import javax.lang.model.element.Name;

public final class NameImpl implements Name {
	
	public static final Name CONSTRUCTOR = new NameImpl("<init>");
	
	private final String name;
	
	public NameImpl(String name) {
		this.name = Objects.requireNonNull(name, "name");
	}

	@Override
	public char charAt(int c) { return name.charAt(c); }

	@Override
	public int length() { return name.length(); }

	@Override
	public CharSequence subSequence(int start, int end) { return name.subSequence(start, end); }

	@Override
	public boolean contentEquals(CharSequence cs) { return name.contentEquals(cs); }
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof NameImpl && this.contentEquals((NameImpl)obj);
	}
	
	@Override
	public int hashCode() { return name.hashCode(); }
	
	@Override
	public String toString() { return name; }
}
