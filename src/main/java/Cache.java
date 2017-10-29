public class Cache<K, V> {

	private DataStore<K, V> dataStore;
	private ContainerSet<K, V>[] containerSets;

	public Cache(int buckets, int slots) {
		this.containerSets = new ContainerSet<>[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			this.containerSets[i] = new ContainerSet(slots);
		}

		this.dataStore = new NoDataStore();
	}

	public Cache(int buckets, int slots, DataStore dataStore) {
		this.containerSets = new ContainerSet<>[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			this.containerSets[i] = new ContainerSet(slots);
		}

		this.dataStore = new dataSore;
		
	}

	public Cache(ContainerSet<K, V>[] containerSets, int buckets, int slots, DataStore<K, V> dataStore) {
		this.dataStore = dataStore;
		this.containerSets = containerSets;
	j

	public void put(K k, V v) {
		
	}

	public V get(K k) {

	}

	public void writeAllToDataStore() {
		
	}

	public void writeAllToDataStore(DataStore dataStore) {
		
	}

	@Override
	public void finalize() {
		writeAllToDataStore();
	}
}
