package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableSet;
import java.util.SortedSet;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {
	
	protected final List<T> arrayList;
	private final Comparator<? super T> comparator;
	
	public ArraySet() {
		this.arrayList = new ArrayList<>();
		this.comparator = null;
	}
	
	public ArraySet(Collection<? extends T> inputCollection) {
		this();
		addAll(inputCollection);
	}
	
	public ArraySet(int initialCapacity) {
		this(initialCapacity, null);
	}
	
	public ArraySet(Comparator<? super T> comparator) {
		this.arrayList = new ArrayList<>();
		this.comparator = comparator;
	}
	
	public ArraySet(int initialCapacity, Comparator<? super T> comparator) {
		this.arrayList = new ArrayList<>(initialCapacity);
		this.comparator = comparator;
	}
	
	public ArraySet(SortedSet<T> sortedSet) {
		this(sortedSet.comparator());
		addAll(sortedSet);
	}
	
	ArraySet(List<T> arrayList, Comparator<? super T> comparator) {
		this.arrayList = arrayList;
		this.comparator = comparator;
	}
	
	@Override
	public boolean add(T val) {
		if (val == null) {
			throw new NullPointerException();
		}
		
		int position = getPosition(val);
		
		return insertAt(position, val);
	}
	
	@Override
	public boolean remove(Object o) {
		@SuppressWarnings("unchecked")
		T val = (T) o;
		
		int position = getPosition(val);
		if (position >= 0) {
			arrayList.remove(position);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean contains(Object o) {
		@SuppressWarnings("unchecked")
		T val = (T) o;
		
		int position = getPosition(val);
		
		return position >= 0;
	}
	
	final int getPosition(T val) {
		int position = 0;
		if (comparator != null) {
			position = Collections.binarySearch(arrayList, val, comparator);
		} else {
			@SuppressWarnings("unchecked")
			List<? extends Comparable<? super T>> comparableList =
										(List<? extends Comparable<? super T>>) arrayList;
			position = Collections.binarySearch(comparableList, val);
		}
		return position;
	}
	
	private boolean insertAt(int position, T val) {
		if (position < 0) {
			arrayList.add(decodeInsPos(position), val);
			return true;
		}
		return false;
	}
	
	private int decodeInsPos(int position) {
		return -position - 1;
	}
	
	@Override
	public void clear() {
		arrayList.clear();
	}
	
	@Override
	public boolean isEmpty() {
		return arrayList.isEmpty();
	}
	
	@Override
	public final Comparator<? super T> comparator() {
		return comparator;
	}
	
	@Override
	public Iterator<T> iterator() {
		return arrayList.iterator();
	}

	@Override
	public T first() {
		return arrayList.get(0);
	}

	@Override
	public T last() {
		return arrayList.get(size() - 1);
	}
	
	@Override
	public T ceiling(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return arrayList.get(position);
		}
		
		return getHigher(position);
	}

	@Override
	public T floor(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return arrayList.get(position);
		}
		
		return getLower(position);
	}

	@Override
	public T higher(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return (position < size() - 1) ? arrayList.get(position + 1) : null;
		}
		
		return getHigher(position);
	}

	@Override
	public T lower(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return (position > 0) ? arrayList.get(position - 1) : null;
		}
		
		return getLower(position);
	}
	
	private T getHigher(int position) {
		position = decodeInsPos(position);
		if (position == size()) {
			return null;
		}
		
		return arrayList.get(position);
	}
	
	private T getLower(int position) {
		position = decodeInsPos(position);
		if (position == 0) {
			return null;
		}
		
		return arrayList.get(position - 1);
	}

	@Override
	public T pollFirst() {
		T val = arrayList.get(0);
		arrayList.remove(0);
		return val;
	}

	@Override
	public T pollLast() {
		T val = arrayList.get(size() - 1);
		arrayList.remove(size() - 1);
		return val;
	}
	
	@Override
	public final int size() {
		return arrayList.size();
	}
	
	@Override
	public Iterator<T> descendingIterator() {
		return new Iterator<T>() {
			private ListIterator<T> backingIterator = arrayList.listIterator(size());
			
			@Override
			public boolean hasNext() {
				return backingIterator.hasPrevious();
			}

			@Override
			public T next() {
				return backingIterator.previous();
			}
			
		};
	}

	@Override
	public NavigableSet<T> descendingSet() {
		return new DescendingArraySubSet<T>(arrayList, null, null,
				comparator, true, true, true, true);
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return this.subSet(fromElement, true, toElement, false);
	}

	@Override
	public NavigableSet<T> subSet(final T fromElement, boolean fromInclusive, final T toElement, boolean toInclusive)
	{
		return new AscendingArraySubSet<T>(arrayList, fromElement, toElement, comparator,
				false, false, fromInclusive, toInclusive);
	}
	
	@Override
	public SortedSet<T> headSet(T toElement) {
		return this.headSet(toElement, false);
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return new AscendingArraySubSet<T>(arrayList, null, toElement, comparator,
				true, false, true, inclusive);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return this.tailSet(fromElement, true);
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return new AscendingArraySubSet<T>(arrayList, fromElement, null, comparator,
				false, true, inclusive, true);
	}
}
