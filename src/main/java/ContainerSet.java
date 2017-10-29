import java.util.LinkedList;

public abstract class ContainerSet<K,V> {
	private Container<K, V>[] containers;
	private int size;

	public ContainerSet(int slots) {
		containers = new Container[slots];
	}

	public void insert(K k, V v, boolean dirty) {
		for (int i = 0; i < containers.length; i++) {
			if (containers[i] != null) {
				if (containers[i].key.equals(k)) {
					containers[i].value = v;
					containers[i].updateTime();
					return;
				}
			}
		}

		int index = 0;
		if (size == containers.length) { 
			index = evict();
		} else {
			for (int i = 0; i < containers.length; i++) {	
				if (containers[i] == null) { 
					index = i;
					break;
				}
			}
		}
		containers[index] = new Container<>(k, v, dirty);
	}

	public V get(K key) {
		for (Container<K, V> c : containers) {
			if (c.key.equals(key))
				return c.value;
		}

		return null;
	}

	public LinkedList<Container<K, V>> getDirtyElements() {
		LinkedList<Container<K, V>> returnValue = new LinkedList<>();
		for (Container<K, V> c : containers) {
			if (c.dirty) {
				returnValue.add(c);
			}
		}

		return returnValue;
	}

	public abstract int evict();

	public Container<K, V>[] getContainers() {
		return containers;
	}
}
