package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

/**
 * Implementation of absolutely ascending subset backed by the array set
 * @see ArraySubSet
 * @author Serge
 */
final class AscendingArraySubSet<T> extends ArraySubSet<T> {

	public AscendingArraySubSet(List<T> backingList, T fromElem, T toElem, Comparator<? super T> comp,
			boolean fromStart, boolean toEnd, boolean fromInclusive, boolean toInclusive, boolean isImmutable)
	{
		super(backingList, fromElem, toElem, comp, fromStart, toEnd, fromInclusive, toInclusive, isImmutable);
	}
	
	@Override
	public Iterator<T> iterator() { return absAscIterator(); }
	
	@Override
	public Iterator<T> descendingIterator() { return absDescIterator(); }
	
	@Override
	public NavigableSet<T> descendingSet() {
		return new DescendingArraySubSet<>(arrayList, fromElem, toElem,
				comparator(), fromStart, toEnd, fromInclusive, toInclusive, isImmutable);
	}
	
	@Override
	public T first() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return get(absIndexOfFirst());
	}
	
	@Override
	public T last() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return get(absIndexOfLast());
	}
	
	@Override
	public T ceiling(T e) { return get(absIndexOfCeiling(e)); }
	
	@Override
	public T floor(T e) { return get(absIndexOfFloor(e)); }
	
	@Override
	public T higher(T e) { return get(absIndexOfHigher(e)); }
	
	@Override
	public T lower(T e) { return get(absIndexOfLower(e)); }
	
	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		if (!isCorrectBound(fromElement, fromInclusive, true))
		{
			throw new IllegalArgumentException("fromElement out of range");
		}
		if (!isCorrectBound(toElement, toInclusive, false)) {
			throw new IllegalArgumentException("toElement out of range");
		}
		return super.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}
	
	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		if (!isCorrectBound(toElement, inclusive, false)) {
			throw new IllegalArgumentException("toElement out of range");
		}
		return new AscendingArraySubSet<>(arrayList, this.fromElem, toElement,
				comparator(), this.fromStart, false, this.fromInclusive, inclusive, isImmutable);
	}
	
	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		if (!isCorrectBound(fromElement, inclusive, true))
		{
			throw new IllegalArgumentException("fromElement out of range");
		}
		return new AscendingArraySubSet<>(arrayList, fromElement, this.toElem,
				comparator(), false, this.toEnd, inclusive, this.toInclusive, isImmutable);
	}
}