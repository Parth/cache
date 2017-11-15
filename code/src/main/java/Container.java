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
	 * Update this Container's last_accessed time using System.nanoTime()
	 */ 
	public abstract boolean update();
}
