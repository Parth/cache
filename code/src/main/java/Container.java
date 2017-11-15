/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 * A Key Value pair that keeps track of when it was last accessed, as well as if
 * It's "clean/dirty" in relation to it's corresponding database.
 */
public abstract class Container<K,V> implements Comparable<Container> {
	public K key;
	public V value; 
	public boolean dirty;

	/**
	 * Create a new Container with a key, value, dirty status.
	 *
	 * @param key 
	 * @param value
	  @param dirty indicates whether this element needs to be written to a dataStore during an eviction event or cache shutdown.
	 * 
	 */
	public Container(K key, V value, boolean dirty) {
		this.key = key; 
		this.value = value;
		this.dirty = dirty;
	}

	/**
	 * Update this Container's last_accessed time using System.nanoTime()
	 */ 
	public abstract boolean update();
}
