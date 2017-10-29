public interface DataStore<K, V> {
	void put(K k, V v);
	V get(K k);
}
