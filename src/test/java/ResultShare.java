interface ResultShare<K, V> {
	void notifyPut(K k, V v);
	void notifyGet(K k);
}
