package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

abstract class ArraySubSet<T> extends ArraySet<T> {
	final T fromElem, toElem;
	final boolean fromStart, toEnd;
	final boolean fromInclusive, toInclusive;
	
	/**
	 * TODO
	 * @param e1
	 * @param e2
	 * @param comp
	 * @return
	 * @throws ClassCastException
	 */
	private static <U> int compare(U e1, U e2, Comparator<? super U> comp) {
		if (comp != null) {
			return comp.compare(e1, e2);
		}
		
		// Small trick to permit nulls
		if (e1 == null) {
			if (e2 == null) {
				return 0;
			}
			U t = e1; e1 = e2; e2 = t;
		}
		
		@SuppressWarnings("unchecked")
		Comparable<? super U> comparableElem1 = (Comparable<? super U>) e1;
		
		return comparableElem1.compareTo(e2);
	}
	
	/**
	 * 
	 * @param backingList
	 * @param fromElem
	 * @param toElem
	 * @param comp
	 * @param fromStart
	 * @param toEnd
	 * @param fromInclusive
	 * @param toInclusive
	 * @param isImmutable
	 * @throws ClassCastException
	 * @throws IllegalArgumentException
	 */
	public ArraySubSet(List<T> backingList, T fromElem, T toElem, Comparator<? super T> comp,
			boolean fromStart, boolean toEnd, boolean fromInclusive, boolean toInclusive, boolean isImmutable)
	{
		super(backingList, comp, isImmutable);
		
		if (!fromStart && !toEnd) {
			if (compare(fromElem, toElem, comp) > 0) {
				throw new IllegalArgumentException("fromElement > toElement");
			}
		} else {
			// Type check
			if (!fromStart) {
				compare(fromElem, fromElem, comp);
			}
			if (!toEnd) {
				compare(toElem, toElem, comp);
			}
		}
		
		this.fromElem = fromElem; this.toElem = toElem;
		this.fromStart = fromStart; this.toEnd = toEnd;
		this.fromInclusive = fromInclusive; this.toInclusive = toInclusive;
	}
	
	// *** Utility methods for checking range ***
	final boolean tooLow(T e) {
        if (!fromStart) {
            int c = compare(e, fromElem, comparator());
            if (c < 0 || (c == 0 && !fromInclusive))
                return true;
        }
        return false;
    }

    final boolean tooHigh(T e) {
        if (!toEnd) {
            int c = compare(e, toElem, comparator());
            if (c > 0 || (c == 0 && !toInclusive))
                return true;
        }
        return false;
    }
    
    final boolean inRange(T e) {
        return !tooLow(e) && !tooHigh(e);
    }
    
    final boolean isCorrectBound(T e, boolean inclusive, boolean left) {
    	return inRange(e) || (!inclusive &&
				ArraySubSet.compare(e,
						left ? this.fromElem : this.toElem, comparator()) == 0);
    }
    
    // *** Methods which implementation depends on ascending/descending order ***
    public abstract Iterator<T> iterator();
    public abstract Iterator<T> descendingIterator();
    public abstract T first();
    public abstract T last();
    public abstract T ceiling(T e);
    public abstract T floor(T e);
    public abstract T higher(T e);
    public abstract T lower(T e);
    public abstract NavigableSet<T> descendingSet();
    /* Cannot make these methods abstract because AscendingArraySubSet needs access to super.subSet(), etc.
    public abstract NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive);
    public abstract NavigableSet<T> headSet(T toElement, boolean inclusive);
    public abstract NavigableSet<T> tailSet(T fromElement, boolean inclusive);
    */
    
    // *** Common methods ***
    @Override
    public boolean isEmpty() {
    	return this.size() == 0;
    }
    
    @Override
    public int size() {
    	if (fromStart && toEnd) {
    		return super.size();
    	}
    	
    	int start = 0, end = super.size() - 1;
    	if (!fromStart) {
    		start = absIndexOfFirst();
    		if (start < 0) {
    			return 0;
    		}
    	}
    	if (!toEnd) {
    		end = absIndexOfLast();
    	}
    	
    	return end - start + 1;
    }
    
    @Override
    public T pollFirst() {
    	T val = this.first();
    	this.remove(val);
    	
    	return val;
    }
    
    @Override
    public T pollLast() {
    	T val = this.last();
    	this.remove(val);
    	
    	return val;
    }
    
    @Override
    public boolean add(T e) {
    	if (!inRange(e))
    		throw new IllegalArgumentException("element out of range");
    	
    	return super.add(e);
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
    	boolean changed = false;
    	for (T e : c) {
    		changed |= (inRange(e) && super.add(e));
    	}
    	
    	return changed;
    }
    
    @Override
    public boolean contains(Object o) {
    	@SuppressWarnings("unchecked")
		T val = (T) o;
    	
    	return inRange(val) && super.contains(val);
    }
    
    @Override
    public boolean remove(Object o) {
    	@SuppressWarnings("unchecked")
		T val = (T) o;
    	
    	return inRange(val) && super.remove(val);
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
    	boolean changed = false;
    	for (Object o : c) {
    		@SuppressWarnings("unchecked")
    		T val = (T) o;
    		changed |= (inRange(val) && super.remove(val));
    	}
    	
    	return changed;
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
    	if (isImmutable) {
    		throw new UnsupportedOperationException("Set is immutable");
    	}
    	
    	boolean changed = false;
    	for(int i = absIndexOfFirst(); i <= absIndexOfLast(); i++) {
    		if (!c.contains(arrayList.get(i))) {
    			arrayList.remove(i--);
    			changed = true;
    		}
    	}
    	
    	return changed;
    }
    
    @Override
    public Object[] toArray() {
    	return this.toArray(null);
    }
    
    @Override
    public <U> U[] toArray(U[] a) {
    	Object[] buf = a;
    	int size = this.size();
    	if (a == null || size > a.length)
    		buf = new Object[size];
    	
    	int i = 0;
    	for (Iterator<T> it = this.iterator(); it.hasNext(); i++) {
    		buf[i] = it.next();
    	}
    	
    	@SuppressWarnings("unchecked")
    	U[] result = (U[]) buf;
    	return result;
    }
    
    // *** Utilities which used in subclasses ***
    final int absIndexOfFirst() {
    	int i = fromStart ? ((super.size() > 0) ? 0 : -1) :
    			(fromInclusive ? super.indexOfCeiling(fromElem) : super.indexOfHigher(fromElem));
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooHigh(val) ? -1 : i;
    }
    
    final int absIndexOfLast() {
    	int i = toEnd ? (super.size() - 1) :
    			(toInclusive ? super.indexOfFloor(toElem) : super.indexOfLower(toElem));
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooLow(val) ? -1 : i;
    }
    
    final int absIndexOfCeiling(T e) {
    	if (tooLow(e)) {
    		return absIndexOfFirst();
    	}
    	
    	int i = super.indexOfCeiling(e);
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooHigh(val) ? -1 : i;
    }
    
    final int absIndexOfFloor(T e) {
    	if (tooHigh(e)) {
    		return absIndexOfLast();
    	}
    	
    	int i = super.indexOfFloor(e);
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooLow(val) ? -1 : i;
    }
    
    final int absIndexOfHigher(T e) {
    	if (tooLow(e)) {
    		return absIndexOfFirst();
    	}
    	
    	int i = super.indexOfHigher(e);
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooHigh(val) ? -1 : i;
    }
    
    final int absIndexOfLower(T e) {
    	if (tooHigh(e)) {
    		return absIndexOfLast();
    	}
    	
    	int i = super.indexOfLower(e);
    	if (i < 0) {
    		return -1;
    	}
    	
    	T val = get(i);
    	return tooLow(val) ? -1 : i;
    }
    
    final Iterator<T> absAscIterator() {
    	final ListIterator<T> backingIterator = listIterator(false);
    	return new Iterator<T>() {
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
    
    final Iterator<T> absDescIterator() {
    	final ListIterator<T> backingIterator = listIterator(true);
    	return new Iterator<T>() {
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
    
    private ListIterator<T> listIterator(boolean fromEnd) {
    	final int start = absIndexOfFirst(), end = absIndexOfLast();
    	return new ListIterator<T>() {
    		private ListIterator<T> backingIterator =
    				fromEnd ? ((end >= 0) ? arrayList.listIterator(end + 1) : null) :
    						((start >= 0) ? arrayList.listIterator(start) : null);
    		
			@Override
			public boolean hasNext() {
				if (backingIterator == null || !backingIterator.hasNext()) {
					return false;
				}
				
				T val = backingIterator.next();
				boolean hasNext = !tooHigh(val);
				
				backingIterator.previous();
				return hasNext;
			}

			@Override
			public boolean hasPrevious() {
				if (backingIterator == null || !backingIterator.hasPrevious()) {
					return false;
				}

				T val = backingIterator.previous();
				boolean hasPrevious = !tooLow(val);
				
				backingIterator.next();
				return hasPrevious;
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				return backingIterator.next();
			}

			@Override
			public T previous() {
				if (!hasPrevious()) {
					throw new NoSuchElementException();
				}
				
				return backingIterator.previous();
			}
			
			// Unused methods
			public int nextIndex() { return 0; }
			public int previousIndex() { return 0; }
			public void add(T e) {}
			public void remove() {}
			public void set(T e) {}
    	};
    }
}