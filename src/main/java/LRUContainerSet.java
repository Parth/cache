public class LRUContainerSet<K, V> extends ContainerSet<K, V> {

	public LRUContainerSet(int slots) {
		super(slots);
	}

	@Override
	public int evict() {
		if (size() < getContainers().length) 
			return -1;

		Container<K, V>[] containers = super.getContainers();
		int index = 0;
		long min = System.nanoTime();
		for (int i = 0; i < containers.length; i++) {
			Container<K, V> c = containers[i];
			System.out.println(c.last_accessed);
			System.out.println(min);
			if (c.last_accessed < min) {
				index = i;
				min = c.last_accessed;
			}
		}

		return index;
	}
}
