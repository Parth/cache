import java.util.LinkedList;

public abstract class ContainerSet<K,V> {
	private Container<K, V>[] containers;
	private int size;

	public ContainerSet(Integer slots) {
		containers = new Container[slots];
	}

	public int insert(K k, V v, boolean dirty) {
		return insert(k, v, dirty, new NoDataStore());
	}

	public int insert(K k, V v, boolean dirty, DataStore dataStore) {
		for (int i = 0; i < containers.length; i++) {
			if (containers[i] != null) {
				if (containers[i].key.equals(k)) {
					containers[i].value = v;
					containers[i].dirty = true;
					containers[i].updateTime();
					return i;
				}
			}
		}

		int index = 0;
		if (size == containers.length) { 
			index = evict();

			if (containers[index].dirty) {
				dataStore.put(containers[index].key, containers[index].value);
			}

			containers[index] = null;
			size--;
		} else {
			for (int i = 0; i < containers.length; i++) {	
				if (containers[i] == null) { 
					index = i;
					break;
				}
			}
		}

		size++;
		containers[index] = new Container(k, v, dirty);
		containers[index].updateTime();
		return index;
	}

	public V get(K key) {
		for (Container<K, V> c : containers) {
			if (c != null) {
				if (c.key.equals(key)) {
					c.updateTime();
					return c.value;
				}
			}
		}

		return null;
	}

	public LinkedList<Container<K, V>> getDirtyElements() {
		LinkedList<Container<K, V>> returnValue = new LinkedList<>();
		for (Container<K, V> c : containers) {
			if (c != null && c.dirty) {
				returnValue.add(c);
			}
		}

		return returnValue;
	}

	public abstract int evict();

	public Container<K, V>[] getContainers() {
		return containers;
	}

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		String returnValue = "";
		for (int i = 0; i < containers.length; i++) {
			if (containers[i] != null) {
				if (i != 0) returnValue += ", ";
				returnValue += "[" + containers[i].key.toString() + "->" + containers[i].value.toString() + ", " + containers[i].last_accessed + ", " + containers[i].dirty + "]";
			}
		}

		return returnValue;
	}
}
