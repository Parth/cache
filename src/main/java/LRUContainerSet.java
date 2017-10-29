public class LRUContainerSet<K, V> extends ContainerSet<K, V> {

	public LRUContainerSet(int slots) {
		super(slots);
	}

	@Override
	public int evict() {
		Container<K, V>[] containers = super.getContainers();
		int index = 0;
		long min = System.currentTimeMillis();
		for (int i = 0; i < containers.length; i++) {
			Container<K, V> c = containers[i];
			if (c.last_accessed < min) {
				index = i;
				min = c.last_accessed;
			}
		}

		containers[index] = null;
		return index;
	}
}
