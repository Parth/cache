/**
 * An interface to connect any other key value store to this cache. 
 */
public interface DataStore<K, V> {
	/** 
	 * Put a key value that is being removed from the cache into the datastore
	 * 
	 * This method can be called during an eviction, or cache shutdown
	 *
	 * @param k key
	 * @param v value
	 */
	void put(K k, V v);

	/** 
	 * A value wasn't in the cache, search the datastore for it
	 * 
	 * @param k key 
	 */
	V get(K k);
}
