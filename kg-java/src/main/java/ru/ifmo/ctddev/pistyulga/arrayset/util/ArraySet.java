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
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {
	
	private final Comparator<? super T> comparator;
	final List<T> arrayList;
	final boolean isImmutable;
	
	public ArraySet() {
		this.arrayList = new ArrayList<>();
		this.comparator = null;
		this.isImmutable = false;
	}
	
	public ArraySet(SortedSet<T> sortedSet) {
		this(sortedSet, sortedSet.comparator());
	}
	
	public ArraySet(Collection<? extends T> inputCollection) {
		this(inputCollection, null);
	}
	
	public ArraySet(Collection<? extends T> inputCollection, Comparator<? super T> comparator) {
		this.arrayList = new ArrayList<>();
		this.comparator = comparator;
		addAll(inputCollection);
		this.isImmutable = CollectionUtil.isImmutable(inputCollection);
	}
	
	public ArraySet(int initialCapacity) {
		this(initialCapacity, null);
	}
	
	public ArraySet(Comparator<? super T> comparator) {
		this.arrayList = new ArrayList<>();
		this.comparator = comparator;
		this.isImmutable = false;
	}
	
	public ArraySet(int initialCapacity, Comparator<? super T> comparator) {
		this.arrayList = new ArrayList<>(initialCapacity);
		this.comparator = comparator;
		this.isImmutable = false;
	}
	
	// This constructor is used internally
	ArraySet(List<T> arrayList, Comparator<? super T> comparator, boolean isImmutable) {
		this.arrayList = arrayList;
		this.comparator = comparator;
		this.isImmutable = isImmutable;
	}
	
	@Override
	public boolean add(T val) {
		if (isImmutable) {
			throw new UnsupportedOperationException("Set is immutable");
		}
		
		int position = getPosition(val);
		
		return insertAt(position, val);
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for (T e : c) {
			changed |= this.add(e);
		}
		
		return changed;
	}
	
	@Override
	public boolean remove(Object o) {
		if (isImmutable) {
			throw new UnsupportedOperationException("Set is immutable");
		}
		
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
	public boolean retainAll(Collection<?> c) {
		if (isImmutable) {
    		throw new UnsupportedOperationException("Set is immutable");
    	}
		return super.retainAll(c);
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
	
	// *** Simple "one-row" methods ***
	@Override
	public T pollFirst() {
		if (isImmutable) {
			throw new UnsupportedOperationException("Set is immutable");
		}
		return this.isEmpty() ? null : arrayList.remove(0);
	}

	@Override
	public T pollLast() {
		if (isImmutable) {
			throw new UnsupportedOperationException("Set is immutable");
		}
		return this.isEmpty() ? null : arrayList.remove(size() - 1);
	}
	
	@Override
	public int size() { return arrayList.size(); }
	
	@Override
	public void clear() {
		if (isImmutable) {
			throw new UnsupportedOperationException("Set is immutable");
		}
		arrayList.clear();
	}
	
	@Override
	public boolean isEmpty() { return arrayList.isEmpty(); }
	
	@Override
	public final Comparator<? super T> comparator() { return comparator; }
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Iterator<T> backingIterator = arrayList.iterator();
			@Override
			public boolean hasNext() {
				return backingIterator.hasNext();
			}
			@Override
			public T next() {
				return backingIterator.next();
			}
			@Override
			public void remove() {
				if (isImmutable) {
					throw new UnsupportedOperationException("Set is immutable");
				}
				backingIterator.remove();
			}
		};
	}

	@Override
	public T first() {
		if (arrayList.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return arrayList.get(0);
	}

	@Override
	public T last() {
		if (arrayList.isEmpty()) {
			throw new NoSuchElementException("Set is empty");
		}
		return arrayList.get(size() - 1);
	}
	
	@Override
	public T ceiling(T e) { return get(indexOfCeiling(e)); }

	@Override
	public T floor(T e) { return get(indexOfFloor(e)); }

	@Override
	public T higher(T e) { return get(indexOfHigher(e)); }

	@Override
	public T lower(T e) { return get(indexOfLower(e)); }
	
	@Override
	public Object[] toArray() { return arrayList.toArray(); }
	
	@Override
	public <U> U[] toArray(U[] a) { return arrayList.toArray(a); }
	
	// *** Utility methods ***
	final T get(int i) {
		return (i >= 0) ? arrayList.get(i) : null;
	}
	
	final int indexOfCeiling(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return position;
		}
		
		return getHigherIndex(position);
	}
	
	final int indexOfFloor(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return position;
		}
		
		return getLowerIndex(position);
	}
	
	final int indexOfHigher(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return (position < arrayList.size() - 1) ? (position + 1) : -1;
		}
		
		return getHigherIndex(position);
	}
	
	final int indexOfLower(T e) {
		int position = getPosition(e);
		if (position >= 0) {
			return (position > 0) ? (position - 1) : -1;
		}
		
		return getLowerIndex(position);
	}
	
	private int getHigherIndex(int position) {
		position = decodeInsPos(position);
		if (position == arrayList.size()) {
			return -1;
		}
		
		return position;
	}
	
	private int getLowerIndex(int position) {
		position = decodeInsPos(position);
		if (position == 0) {
			return -1;
		}
		
		return position - 1;
	}
	
	// *** "Complex" methods ***
	@Override
	public Iterator<T> descendingIterator() {
		return new Iterator<T>() {
			private ListIterator<T> backingIterator = arrayList.listIterator(arrayList.size());
			
			@Override
			public boolean hasNext() {
				return backingIterator.hasPrevious();
			}

			@Override
			public T next() {
				return backingIterator.previous();
			}
			
			@Override
			public void remove() {
				if (isImmutable) {
					throw new UnsupportedOperationException("Set is immutable");
				}
				backingIterator.remove();
			}
		};
	}

	@Override
	public NavigableSet<T> descendingSet() {
		return new DescendingArraySubSet<T>(arrayList, null, null,
				comparator, true, true, true, true, isImmutable);
	}
	
	/**
	 * Equivalent to {@link #subSet(Object, boolean, Object, boolean) subSet(fromElement, true, toElement, false)}
	 * that may be overridden
	 */
	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return this.subSet(fromElement, true, toElement, false);
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive)
	{
		return new AscendingArraySubSet<T>(arrayList, fromElement, toElement, comparator,
				false, false, fromInclusive, toInclusive, isImmutable);
	}
	
	/**
	 * Equivalent to {@link #headSet(Object, boolean) headSet(toElement, false)} that may be overridden
	 */
	@Override
	public final SortedSet<T> headSet(T toElement) {
		return this.headSet(toElement, false);
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return new AscendingArraySubSet<T>(arrayList, null, toElement, comparator,
				true, false, true, inclusive, isImmutable);
	}
	
	/**
	 * Equivalent to {@link #tailSet(Object, boolean) tailSet(fromElement, true)} that may be overridden
	 */
	@Override
	public final SortedSet<T> tailSet(T fromElement) {
		return this.tailSet(fromElement, true);
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return new AscendingArraySubSet<T>(arrayList, fromElement, null, comparator,
				false, true, inclusive, true, isImmutable);
	}
}