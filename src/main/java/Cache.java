import java.lang.reflect.InvocationTargetException;

public class Cache<K, V> {

	private DataStore<K, V> dataStore;
	private ContainerSet<K, V>[] containerSets;

	public Cache(int buckets, int slots) {
		this(buckets, slots, new NoDataStore());
	}

	public Cache(int buckets, int slots, DataStore dataStore) {
		this.dataStore = dataStore;
		containerSets = new ContainerSet[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			containerSets[i] = new LRUContainerSet(slots);
		}
	}

	public Cache(Class containerSet, int buckets, int slots, DataStore<K, V> dataStore) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		this.dataStore = dataStore;
		
		containerSets = new ContainerSet[buckets];
		for (int i = 0; i < containerSets.length; i++) {
			containerSets[i] = (ContainerSet<K, V>) containerSet.getDeclaredConstructor(Integer.class).newInstance(slots);
		}
	}

	public int hash(K k) {
		return Math.abs(k.hashCode());
	}

	public void put(K k, V v) {
		ContainerSet<K, V> c = containerSets[ hash(k) % containerSets.length ];
		c.insert(k, v, true, dataStore);
	}

	public V get(K k) {
		ContainerSet<K, V> c = containerSets[ hash(k) % containerSets.length ];
		V v = c.get(k);

		if (v == null) {
			v = dataStore.get(k);
			if (v != null) {
				c.insert(k, v, false, dataStore);
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

	@Override
	public String toString() {
		String returnValue = "";
		for (ContainerSet<K, V> cs : containerSets) {
			returnValue += cs.toString() + "\n";
		}
		return returnValue;
	}
}
