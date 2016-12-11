package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

/**
 * Implementation of absolutely descending subset backed by the array set
 * @see ArraySubSet
 * @author Serge
 */
final class DescendingArraySubSet<T> extends ArraySubSet<T> {

	public DescendingArraySubSet(List<T> backingList, T fromElem, T toElem, Comparator<? super T> comp,
			boolean fromStart, boolean toEnd, boolean fromInclusive, boolean toInclusive, boolean isImmutable)
	{
		super(backingList, fromElem, toElem, comp, fromStart, toEnd, fromInclusive, toInclusive, isImmutable);
	}
	
	@Override
	public Iterator<T> iterator() { return absDescIterator(); }
	
	@Override
	public Iterator<T> descendingIterator() { return absAscIterator(); }
	
	@Override
	public NavigableSet<T> descendingSet() {
		return new AscendingArraySubSet<>(arrayList, fromElem, toElem,
				comparator(), fromStart, toEnd, fromInclusive, toInclusive, isImmutable);
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
		if (!isCorrectBound(toElement, toInclusive, true))
		{
			throw new IllegalArgumentException("fromElement out of range");
		}
		if (!isCorrectBound(fromElement, fromInclusive, false)) {
			throw new IllegalArgumentException("toElement out of range");
		}
		return new DescendingArraySubSet<>(arrayList, toElement, fromElement,
				comparator(), false, false, toInclusive, fromInclusive, isImmutable);
	}
	
	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		if (!isCorrectBound(toElement, inclusive, true)) {
			throw new IllegalArgumentException("toElement out of range");
		}
		return new DescendingArraySubSet<>(arrayList, toElement, this.toElem,
				comparator(), false, this.toEnd, inclusive, this.toInclusive, isImmutable);
	}
	
	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		if (!isCorrectBound(fromElement, inclusive, false)) {
			throw new IllegalArgumentException("fromElement out of range");
		}
		return new DescendingArraySubSet<>(arrayList, this.fromElem, fromElement,
				comparator(), this.fromStart, false, this.fromInclusive, inclusive, isImmutable);
	}
}