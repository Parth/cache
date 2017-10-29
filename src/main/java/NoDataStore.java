public class NoDataStore<K, V> implements DataStore<K, V> {
	@Override
	public void put(K k, V v) {
		System.out.println("Inserted: (" + k + ", " + v + ")");
	}

	@Override
	public V get(K k) {
		System.out.println("Searching for k: " + k);
		return null;
	}
}
