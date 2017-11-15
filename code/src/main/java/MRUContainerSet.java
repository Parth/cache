/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A ContainerSet that models the MRU Replacement Policy
 */
public class MRUContainer<K, V> extends Container<K, V> {

	private long usageCount = 0;

	public MRUContainer(K key, V value, boolean dirty) {
		super(key, value, dirty);
	}

	@Override 
	public int compareTo(

	/**
	 * Evict an element based on it's last_accessed time
	 *
	 * @return index of the element with the largest (most recent) time
	 */
	@Override
	public int evict() {
		if (size() < getContainers().length) 
			return -1;

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

		return index;
	}

}
