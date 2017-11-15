/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A very flexible in-memory N-Way Set Associative Cache. 
 * Stores and retrieves key value pairs. Maintains state relative to a DataStore (another key value store)
 * Communicates with the DataStore through the DataStore interface, allowing you to chain caches across different
 * Deployments, or connect it to any other key value storage system.
 *
 * In general all opperations will be O(m), where m indicates how many "slots" there are per "bucket".
 */
public class Cache<K, V> {

	private DataStore<K, V> dataStore;
	private ContainerSet<K, V>[] containerSets;

	/** 
	 * Create a new Cache with no DataStore connected
	 *
	 * @param buckets what all keys will be mod'd by.
	 * @param slots how many slots per bucket.
	 */
	public Cache(int buckets, int slots) {
		this(buckets, slots, new NoDataStore());
	}

	/**
	 * Create a new Cache with a DataStore connected
	 *
	 * @param buckets what all keys will be mod'd by.
	 * @param slots how many slots per bucket.
	 * @param dataStore dataStore the cache will talk to for persistence 
	 */
	public Cache(int buckets, int slots, DataStore dataStore) {
		this(LRUContainer.class, buckets, slots, dataStore);
	}

	/**
	 * Create a cache with a datastore, and a custom replacement policy
	 *
	 * @param container a Class refernce that inherits from ContainerSet with a custom evict() function. Will be instantiated through reflection
	 * @param buckets what all keys will be mod'd by.
	 * @param slots how many slots per bucket.
	 * @param dataStore dataStore the cache will talk to for persistence 
	 */
	public Cache(Class container, int buckets, int slots, DataStore<K, V> dataStore) {
		this.dataStore = dataStore;
		
		containerSets = new ContainerSet[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			containerSets[i] = new ContainerSet(slots, container);
		}
	}

	/**
	 * A general strategy to retrieve indexes from Object.hashCode()
	 */
	public int hash(K k) {
		return Math.abs(k.hashCode());
	}

	/**
	 * Put a new, dirty, key value into the cache
	 * 
	 * If it evicts an element, the old element gets sent to the DataStore
	 * If this key-pair is ever evicted, it will be written to the DataStore
	 * @param k key
	 * @param v value
	 */
	public void put(K k, V v) {
		ContainerSet<K, V> c = containerSets[ hash(k) % containerSets.length ];
		c.insert(k, v, true, dataStore);
	}

	/**
	 * Retrieve a value based on a key
	 *
	 * If the key is not in the cache, it will check the dataStore and return the result of
	 * dataStore.get(k)
	 *
	 * @param k key
	 * @return V value
	 */
	public V get(K k) {
		ContainerSet<K, V> c = containerSets[ hash(k) % containerSets.length ];
		V v = c.get(k);

		if (v == null) {
			v = dataStore.get(k);
			if (v != null) {
				c.insert(k, v, false, dataStore);
			}
		}

		return v;
	}

	/**
	 * Save all dirty Key-Values to the dataStore
	 */
	public void writeAllToDataStore() {
		this.writeAllToDataStore(this.dataStore);
	}

	/**
	 * Save all dirty Key-Values to a particular dataStore
	 * @param dataStore to write dirty keys too
	 */
	public void writeAllToDataStore(DataStore dataStore) {
		for (ContainerSet<K, V> cs : containerSets) {
			for (Container<K, V> dirty : cs.getDirtyElements()) {
				dataStore.put(dirty.key, dirty.value);
			}
		}
	}

	/**
	 *	writeAllToDataStore() upon garbage collection. Not a reliable way to teardown a cache.
	 */
	@Override
	public void finalize() {
		writeAllToDataStore();
	}

	@Override
	public String toString() {
		String returnValue = "";
		for (ContainerSet<K, V> cs : containerSets) {
			returnValue += cs.toString() + "\n";
		}
		return returnValue;
	}
}
