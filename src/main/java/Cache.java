public class Cache<K, V> {

	private DataStore<K, V> dataStore;
	private ContainerSet<K, V>[] containerSets;

	public Cache(int buckets, int slots) {
		this.containerSets = new ContainerSet[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			this.containerSets[i] = new LRUContainerSet(slots);
		}

		this.dataStore = new NoDataStore();
	}

	public Cache(int buckets, int slots, DataStore dataStore) {
		this(buckets, slots);
		this.dataStore = dataStore;
	}

	public Cache(ContainerSet<K, V>[] containerSets, int buckets, int slots, DataStore<K, V> dataStore) {
		this(buckets, slots, dataStore);
		this.containerSets = containerSets;
	}

	public void put(K k, V v) {
		ContainerSet<K, V> c = containerSets[ k.hashCode() % containerSets.length ];
		c.insert(k, v, true);
	}

	public V get(K k) {
		ContainerSet<K, V> c = containerSets[ k.hashCode() % containerSets.length ];
		V v = c.get(k);

		if (v == null) {
			v = dataStore.get(k);
			if (v != null) {
				c.insert(k, v, false);
			}
		}

		return v;
	}

	public void writeAllToDataStore() {
		this.writeAllToDataStore(this.dataStore);
	}

	public void writeAllToDataStore(DataStore dataStore) {
		for (ContainerSet<K, V> cs : containerSets) {
			for (Container<K, V> dirty : cs.getDirtyElements()) {
				dataStore.put(dirty.key, dirty.value);
			}
		}
	}

	@Override
	public void finalize() {
		writeAllToDataStore();
	}
}
