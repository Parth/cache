public class LRUContainerSet<K, V> extends ContainerSet<K, V> {

	public LRUContainerSet(int slots) {
		super(slots):
	}

	@Override
	public int evict() {
		containers = super.getContainers();
		int index = 0;
		int min = System.currentTimeMillis();
		for (int i = 0; i < containers[i]; i++) {
			ContainerSet<K, V> c = containers[i];
			if (c.last_accessed < min) {
				index = i;
				min = c.last_accessed;
			}
		}

		containers[index] = null;
		return index;
	}
}
