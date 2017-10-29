public class MRUContainerSet<K, V> extends ContainerSet<K, V> {

	public MRUContainerSet(int slots) {
		super(slots);
	}

	@Override
	public int evict() {
		Container<K, V>[] containers = super.getContainers();
		int index = 0;
		long max = 0;
		for (int i = 0; i < containers.length; i++) {
			Container<K, V> c = containers[i];
			if (c.last_accessed > max) {
				index = i;
				max = c.last_accessed;
			}
		}

		containers[index] = null;
		return index;
	}

}
