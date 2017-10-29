public class NoDataStore<K, V> implements DataStore<K, V> {
	@Override
	public void put(K k, V v) { }

	@Override
	public V get(K k) {
		return null;
	}
}
