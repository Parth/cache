/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A ContainerSet that models the LRU Replacement Policy
 */
public class LRUContainer<K, V> extends Container<K, V> {

	public Long count = 0L;

	LRUContainer() {}

	public LRUContainer(K k, V v, boolean dirty) {
		super(k, v, dirty);
	}

	@Override
	public boolean update() {
		count++;
		return true;
	}

	@Override
	public int compareTo(Container o) {
		return this.count.compareTo(((LRUContainer) o).count);
	}
}
