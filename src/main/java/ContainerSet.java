public abstract class ContainerSet<K,V> {
	private Container<K, V>[] containers;
	private int size;

	public class ContainerSet(int slots) {
		containers = new Container<>[slots];
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

	public int evict();

	public Container<K, V>[] getContainers() {
		return containers;
	}
}
