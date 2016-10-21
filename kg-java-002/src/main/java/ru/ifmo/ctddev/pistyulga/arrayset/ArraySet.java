package ru.ifmo.ctddev.pistyulga.arrayset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableSet;
import java.util.SortedSet;

@SuppressWarnings("serial")
public final class ArraySet<T> extends ArrayList<T> implements NavigableSet<T> {
	
	private final Comparator<? super T> comparator;
	
	public ArraySet() {
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
		this.comparator = comparator;
	}
	
	public ArraySet(int initialCapacity, Comparator<? super T> comparator) {
		super(initialCapacity);
		this.comparator = comparator;
	}
	
	public ArraySet(SortedSet<T> sortedSet) {
		this(sortedSet.comparator());
		addAll(sortedSet);
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
	public void add(int index, T element) {
		throw new UnsupportedOperationException("Specifying position is not allowed");
	}
	
	@Override
	public boolean addAll(Collection<? extends T> inputCollection) {
		Iterator<? extends T> inputCollectionIterator = inputCollection.iterator();
		boolean changed = false;
		while (inputCollectionIterator.hasNext()) {
			T val = inputCollectionIterator.next();
			changed |= this.add(val);
		}
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException("Specifying position is not allowed");
	}
	
	private int getPosition(T val) {
		int position = 0;
		if (comparator != null) {
			position = Collections.binarySearch(this, val, comparator);
		} else {
			@SuppressWarnings("unchecked")
			List<? extends Comparable<? super T>> comparableList = (List<? extends Comparable<? super T>>) this;
			position = Collections.binarySearch(comparableList, val);
		}
		return position;
	}
	
	private boolean insertAt(int position, T val) {
		if (position < 0) {
			super.add(-position - 1, val);
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
	
	@Override
	public Comparator<? super T> comparator() {
		return comparator;
	}

	@Override
	public T first() {
		return super.get(0);
	}

	@Override
	public T last() {
		return super.get(size() - 1);
	}

	@Override
	public T ceiling(T e) {
		// TODO
		return null;
	}

	@Override
	public Iterator<T> descendingIterator() {
		final List<T> thisList = this;
		return new Iterator<T>() {
			private ListIterator<T> backingIterator = thisList.listIterator();
			
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
		// TODO
		return null;
	}

	@Override
	public T floor(T e) {
		// TODO
		return null;
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		// TODO
		return null;
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		// TODO
		return null;
	}

	@Override
	public T higher(T e) {
		// TODO
		return null;
	}

	@Override
	public T lower(T e) {
		// TODO
		return null;
	}

	@Override
	public T pollFirst() {
		// TODO
		return null;
	}

	@Override
	public T pollLast() {
		// TODO
		return null;
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		// TODO
		return null;
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		// TODO
		return null;
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		// TODO
		return null;
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		// TODO
		return null;
	}

}
