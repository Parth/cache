/** 
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A dummy DataStore used as a placeholder. Does not save or retrieve anything
 */
public class NoDataStore<K, V> implements DataStore<K, V> {
	@Override
	public void put(K k, V v) { }

	@Override
	public V get(K k) {
		return null;
	}
}
