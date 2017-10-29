/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A ContainerSet that models the LRU Replacement Policy
 */
public class LRUContainerSet<K, V> extends ContainerSet<K, V> {

	public LRUContainerSet(Integer slots) {
		super(slots);
	}

	/**
	 * Evict an element based on it's last_accessed time
	 *
	 * @return index of the element with the smallest (most distant) time
	 */
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
