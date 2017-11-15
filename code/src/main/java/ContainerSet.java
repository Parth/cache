import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 * 
 * ContainerSet manages a Set of KeyValue pairs.
 * It's responsible for doing all store/retrieval opperations, as well as
 * handling any eviction events. Upon eviction, if it evicts a "Dirty" (see Container.java)
 * Container, it writes it to the DataStore.
 * All operations within this class are generally O(n), where n is the number of slots.
 */ 
public class ContainerSet<K,V> {
	private PriorityQueue<Container<K, V>> containers;
	private int size;
	private Class containerClass;

	/**
	 * Create a new ContainerSet that holds n elements
	 * @param slots, the maximum number of elements this collection can hold. Also the threshold for evict() events.
	 */
	public ContainerSet(Integer slots, Class containerClass) {
		containers = new PriorityQueue<>(slots);
		this.containerClass = containerClass;
	}

	/**
	 * Inserts a key, value pair into the set. 
	 * 
	 * This method uses NoDataStore(). Data may be lost during evict events. See insert(K, V, boolean, DataStore)
	 *
	 * Generally O(n) + D where m is slots, and D is time taken by datastore
	 * 
	 * @param k key of the value being saved
	 * @param v value of the key being saved
	 * @param dirty wether this object needs to be written to the database.
	 * @param dataStore for evicted data
	 */
	public void insert(K k, V v, boolean dirty, DataStore dataStore) {
		if (size == containers.size()) { // evict?
			Container<K, V> container = containers.poll(); // O(log(n))

			if (container.dirty) { // write to datastore?
				dataStore.put(container.key, container.value);
			}

			size--;
		} 

		size++;
		try {
			containers.offer((Container<K, V>) containerClass.getDeclaredConstructor(GenericDeclaration.class, GenericDeclaration.class, Boolean.class).newInstance(k, v, dirty));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} // O(log(n))
	}

	/**
	 * Get Value by Key
	 * 
	 * @param key associated with value
	 * @return V or null if not in Set
	 */
	public V get(K key) {
		Container<K, V> ret = null;
		boolean wasUpdated = false;
		Iterator<Container<K, V>> it = containers.iterator();

		while (it.hasNext()) { // O(n)
			Container c = it.next();

			if (c.key.equals(key)) {
				ret = c;
				wasUpdated = ret.update();
				if (wasUpdated) {
					it.remove(); // O(1) 
				}
			}
		}

		if (wasUpdated) {
			containers.add(ret); // reinsert and let heap prioritize O(log(n))
		}

		return (ret == null) ? null : ret.value;
	}

	/**
	 * @return all elements that currently need to be written to the dataStore
	 */
	public LinkedList<Container<K, V>> getDirtyElements() {
		LinkedList<Container<K, V>> returnValue = new LinkedList<>();
		for (Container<K, V> c : containers) {
			if (c.dirty) {
				returnValue.add(c);
			}
		}

		return returnValue;
	}

	/**
	 * @return all elements currently stored in the set
	 */
	public PriorityQueue<Container<K, V>> getContainers() {
		return containers;
	}

	/**
	 * @return of the slots available, returns how many are currently filled
	 */
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		return containers.toString();
	}
}
