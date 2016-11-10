package ru.ifmo.ctddev.pistyulga.arrayset.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

abstract class ArraySubSet<T> extends ArraySet<T> {
	final T fromElem, toElem;
	final boolean fromStart, toEnd;
	final boolean fromInclusive, toInclusive;
	
	private int compare(T e1, T e2, Comparator<? super T> comp) {
		if (comp != null) {
			return comp.compare(e1, e2);
		}
		
		@SuppressWarnings("unchecked")
		Comparable<? super T> comparableElem1 = (Comparable<? super T>) e1;
		
		return comparableElem1.compareTo(e2);
	}
	
	public ArraySubSet(List<T> backingList, T fromElem, T toElem, Comparator<? super T> comp,
			boolean fromStart, boolean toEnd, boolean fromInclusive, boolean toInclusive)
	{
		super(backingList, comp);
		
		if (!fromStart && !toEnd) {
			if (compare(fromElem, toElem, comp) > 0) {
				throw new IllegalArgumentException("fromElement > toElement");
			}
		} else {
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

    final boolean inClosedRange(T e) {
        return (fromStart || compare(e, fromElem, comparator()) >= 0) &&
        		(toEnd || compare(toElem, e, comparator()) >= 0);
    }

    final boolean inRange(T e, boolean inclusive) {
        return inclusive ? inRange(e) : inClosedRange(e);
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
    
    final T absFirst() {
    	T val = fromStart ? super.first() :
    			(fromInclusive ? super.ceiling(fromElem) : super.higher(fromElem));
    	return (val == null || tooHigh(val)) ? null : val;
    }
    
    final T absLast() {
    	T val = toEnd ? super.last() :
    			(toInclusive ? super.floor(toElem) : super.lower(toElem));
    	return (val == null || tooLow(val)) ? null : val;
    }
    
    final T absCeiling(T e) {
    	// TODO
    	return null;
    }
    
    final T absFloor(T e) {
    	// TODO
    	return null;
    }
    
    final T absHigher(T e) {
    	// TODO
    	return null;
    }
    
    final T absLower(T e) {
    	// TODO
    	return null;
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
				// TODO Auto-generated method stub
				return backingIterator.next();
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
				// TODO Auto-generated method stub
				return backingIterator.previous();
			}
    	};
    }
    
    private ListIterator<T> listIterator(boolean fromEnd) {
    	final T first = absFirst(), last = absLast();
    	return new ListIterator<T>() {
    		private ListIterator<T> backingIterator = arrayList.listIterator(
    				fromEnd ? ((last != null) ? getPosition(last) : null) :
    						((first == null) ? getPosition(first) : null));
    		
			@Override
			public boolean hasNext() {
				if (backingIterator == null || !backingIterator.hasNext()) {
					return false;
				}
				
				T val = backingIterator.next();
				if (tooHigh(val)) {
					return false;
				}
				
				backingIterator.previous();
				return true;
			}

			@Override
			public boolean hasPrevious() {
				if (backingIterator == null || !backingIterator.hasPrevious()) {
					return false;
				}
				
				T val = backingIterator.previous();
				if (tooLow(val)) {
					return false;
				}
				
				backingIterator.next();
				return true;
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
			
			public int nextIndex() { return 0; }
			public int previousIndex() { return 0; }
			public void add(T e) {}
			public void remove() {}
			public void set(T e) {}
    	};
    }
}