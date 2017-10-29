public class Container<K,V>{
	public K key;
	public V value; 
	public long last_accessed;
	public boolean dirty;

	public Container(K key, V value, boolean dirty) {
		this.key = key; 
		this.value = value;
		this.dirty = dirty;
	}

	public void updateTime() {
		last_accessed = System.nanoTime();
	}
}
