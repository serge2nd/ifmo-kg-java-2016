package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

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
	public T first() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return get(absIndexOfLast());
	}
	
	@Override
	public T last() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return get(absIndexOfFirst());
	}
	
	@Override
	public T ceiling(T e) { return get(absIndexOfFloor(e)); }
	
	@Override
	public T floor(T e) { return get(absIndexOfCeiling(e)); }
	
	@Override
	public T higher(T e) { return get(absIndexOfLower(e)); }
	
	@Override
	public T lower(T e) { return get(absIndexOfHigher(e)); }
	
	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		return new DescendingArraySubSet<>(arrayList, fromElement, toElement,
				comparator(), false, false, fromInclusive, toInclusive);
	}
	
	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return new DescendingArraySubSet<>(arrayList, null, toElement,
				comparator(), true, false, true, inclusive);
	}
	
	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return new DescendingArraySubSet<>(arrayList, fromElement, null,
				comparator(), false, true, inclusive, true);
	}
}