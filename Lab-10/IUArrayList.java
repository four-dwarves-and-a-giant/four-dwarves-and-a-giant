import java.util.*;

/**
 * Array-based implementation of IndexedUnsortedList.
 * 
 * @author 
 *
 * @param <E> type to store
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private E[] array;
	private int rear;
	private int modCount; // DO NOT REMOVE ME
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (E[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0; // DO NOT REMOVE ME
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}

	@Override
	public void addToFront(E element) {
		add(0, element);
		modCount++; // DO NOT REMOVE ME
		
	}

	@Override
	public void addToRear(E element) {
		add(element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(E element) {
		add(rear, element);

		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addAfter(E element, E target) {
		if (!contains(target)) { throw new NoSuchElementException(); }
		add(indexOf(target)+1, element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(int index, E element) {
		shiftRight(index);
		array[index] = element;
		modCount++; // DO NOT REMOVE ME
	}


	@Override
	public E removeFirst() {
		E retVal = remove(first());
		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public E removeLast() {
		E retVal = remove(last());
		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public E remove(E element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		E retVal = remove(index);
		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public E remove(int index) {
		E retVal = array[index];
		if (index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public void set(int index, E element) {
		if (index < 0 || index > size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		array[index] = element;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E get(int index) {
		if (index < 0 || index > size() - 1) {
			throw new IndexOutOfBoundsException();
		}

		return array[index];
	}

	@Override
	public int indexOf(E element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

	@Override
	public E first() {
		if (isEmpty()) {throw new NoSuchElementException();}
		return array[0];
	}

	@Override
	public E last() {
		if (isEmpty()) {throw new NoSuchElementException();}
		return array[rear-1];
	}

	@Override
	public boolean contains(E target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public String toString() {
		String result = "[";
		for (int i = 0; i < rear; i++) {
			result += array[i];
			if (i < rear - 1) {
				result += ", ";
			}
		}
		return result + "]";
	}

	private void shiftRight(int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		if (size() == array.length - 1) { expandCapacity(); }
		for (int i = size(); i > index - 1; i--) {
			array[i+1] = array[i];
		}
		rear++;
		array[index] = null;
	}

	// IGNORE THE FOLLOWING COMMENTED OUT CODE UNTIL LAB 10
	// DON'T DELETE ME, HOWEVER!!!
	public Iterator<E> iterator() {
		return new IUArrayListIterator(); // UNCOMMENT ME IN LAB 10
		// return null; // REMOVE ME IN LAB 10 XXX removed DS
	}

	// UNCOMMENT THE CODE BELOW IN LAB 10
	
	private class IUArrayListIterator implements Iterator<E> {

		private int iterModCount, current;
		private boolean canRemove;

		public IUArrayListIterator() {
			iterModCount = modCount;
			current = 0;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return current < rear;
		}

		@Override
		public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = array[current];
			current++;
            canRemove = true;
            return item;
		}

		@Override
		public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            // remove the element in the array at index current-1
            // presumably decrement the rear
            // presumably the modCount is getting incremented
			// all indices have to back up by one
			current--;
			rear--;
			// shift elements to the left
			for (int i = current; i < rear; i++) {
				array[i] = array[i + 1];
			}
			array[rear] = null;
			modCount++;
			iterModCount++;
			// Can only remove the LAST "seen" element
			// set back to a non-removal state 
            canRemove = false;
		}
		
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
