public interface DataStore<K, V> {
	void put(K k, V v);
	V put(K k);
}
