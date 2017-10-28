public class Cache<K, V> {
	private DataStore<K, V> dataStore;
	private int buckets;
	private int slots;

	class ContainerChain {
		K key;
		private V value;
		long last_accessed;
		boolean dirty;
		Container next;

		ContainerChain(K k, V v, boolean d) {
			key = k;
			value = v;
			last_accessed = System.currentTimeMillis();
			dirty = d;
			next = null;
		}

		V getValue() {
			last_accessed = System.currentTimeMillis();
			return value;
		}

		void setValue(V v) {
			dirty = dirty || !v.equals(value);
			value = v;
			last_accessed = System.currentTimeMillis();
		}
	}

	private ContainerChain[] containerChains;

	public Cache(int buckets, int slots, DataStore dataStore) {
		this.dataStore = dataStore;
		this.buckets = buckets;
		this.slots = slots;

		containerChains = new ContainerChain[buckets];
	}

	public void put(K k, V v) {
		insert(k, v, true);
	}

	private void insert(K k, V v, boolean dirty) {
		ContainerChain c = containerChains[ k.hashCode() % buckets ];

		boolean inserted = false;

		if (c == null) {
			containerChains[ k.hashCode() % buckets ] = new ContainerChain(k, v, dirty);
			return;
		} 

		while (!inserted) {
			if (c.key.equals(k)) {
				c.setValue(k);
			} else if {
				if (c.next == null) {
					c.next = new ContainerChain(k, v, dirty);
					return;
				}
				c = c.next;
			}
		}
	}

	public V get(K k) {
		ConatinerChain c = containerChains[ k.hashCode() % buckets ];

		V returnValue = null;

		while (returnValue == null) {
			if (c == null) {
				returnValue = dataStore.get(k);
				if (returnValue != null) {
					insert(k, returnValue, false);
				}

				return returnValue;

			} else if (c.key.equals(k)) {

				return c.getValue();

			} else {
				c = c.next;
			}
		}
	}

	public void writeToDataStore() {
		
	}

	@Override
	public void finalize() {
		writeToDataStore();
	}
}
