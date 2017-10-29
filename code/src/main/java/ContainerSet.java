import java.util.LinkedList;

/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 * 
 * ContainerSet manages a Set of KeyValue pairs.
 * It's responsible for doing all store/retrieval opperations, as well as
 * handling any eviction events. Upon eviction, if it evects a "Dirty" (see Container.java)
 * Container, it writes it to the DataStore.
 * All operations within this class are generally O(n), where n is the number of slots.
 */ 
public abstract class ContainerSet<K,V> {
	private Container<K, V>[] containers;
	private int size;

	/**
	 * Create a new ContainerSet that holds n elements
	 * @param slots, the maximum number of elements this collection can hold. Also the threshold for evict() events.
	 */
	public ContainerSet(Integer slots) {
		containers = new Container[slots];
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
	public int insert(K k, V v, boolean dirty, DataStore dataStore) {
		// Old key, new value?
		for (int i = 0; i < containers.length; i++) {
			if (containers[i] != null) {
				if (containers[i].key.equals(k)) {
					containers[i].value = v;
					containers[i].dirty = true;
					containers[i].updateTime();
					return i;
				}
			}
		}

		// New Key, new Value
		int index = 0;
		if (size == containers.length) { // evict?
			index = evict();

			if (containers[index].dirty) { // write to datastore?
				dataStore.put(containers[index].key, containers[index].value);
			}

			containers[index] = null;
			size--;
		} else { // Use an empty slot
			for (int i = 0; i < containers.length; i++) {	
				if (containers[i] == null) { 
					index = i;
					break;
				}
			}
		}

		size++;
		containers[index] = new Container(k, v, dirty);
		containers[index].updateTime();
		return index; 
	}

	/**
	 * Get Value by Key
	 * 
	 * @param key associated with value
	 * @return V or null if not in Set
	 */
	public V get(K key) {
		for (Container<K, V> c : containers) {
			if (c != null) {
				if (c.key.equals(key)) {
					c.updateTime();
					return c.value;
				}
			}
		}

		return null;
	}

	/**
	 * @return all elements that currently need to be written to the dataStore
	 */
	public LinkedList<Container<K, V>> getDirtyElements() {
		LinkedList<Container<K, V>> returnValue = new LinkedList<>();
		for (Container<K, V> c : containers) {
			if (c != null && c.dirty) {
				returnValue.add(c);
			}
		}

		return returnValue;
	}

	/**
	 * Function that computes which element needs to be evicted
	 * @return index of next element to evict
	 */
	public abstract int evict();

	/**
	 * @return all elements currently stored in the set
	 */
	public Container<K, V>[] getContainers() {
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
		String returnValue = "";
		for (int i = 0; i < containers.length; i++) {
			if (containers[i] != null) {
				if (i != 0) returnValue += ", ";
				returnValue += "[" + containers[i].key.toString() + "->" + containers[i].value.toString() + ", " + containers[i].last_accessed + ", " + containers[i].dirty + "]";
			}
		}

		return returnValue;
	}
}
