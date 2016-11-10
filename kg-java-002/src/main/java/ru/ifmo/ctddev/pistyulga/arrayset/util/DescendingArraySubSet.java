package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;

final class DescendingArraySubSet<T> extends ArraySubSet<T> {

	public DescendingArraySubSet(List<T> backingList, T fromElem, T toElem, Comparator<? super T> comp,
			boolean fromStart, boolean toEnd, boolean fromInclusive, boolean toInclusive)
	{
		super(backingList, fromElem, toElem, comp, fromStart, toEnd, fromInclusive, toInclusive);
	}
	
	@Override
	public Iterator<T> iterator() { return absDescIterator(); }
	
	@Override
	public Iterator<T> descendingIterator() { return absAscIterator(); }
	
	@Override
	public NavigableSet<T> descendingSet() {
		return new AscendingArraySubSet<>(arrayList, fromElem, toElem,
				comparator(), fromStart, toEnd, fromInclusive, toInclusive);
	}
	
	@Override
	public T first() { return absLast(); }
	
	@Override
	public T last() { return absFirst(); }
	
	@Override
	public T ceiling(T e) { return absFloor(e); }
	
	@Override
	public T floor(T e) { return absCeiling(e); }
	
	@Override
	public T higher(T e) { return absLower(e); }
	
	@Override
	public T lower(T e) { return absHigher(e); }
	
	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		// TODO Auto-generated method stub
		return super.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}
	
	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return super.headSet(toElement, inclusive);
	}
	
	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return super.tailSet(fromElement, inclusive);
	}
}
