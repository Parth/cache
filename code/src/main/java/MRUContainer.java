/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A ContainerSet that models the LRU Replacement Policy
 */
public class MRUContainer<K, V> extends Container<K, V> {

	public Long count = 0L;

	public MRUContainer(K k, V v, boolean dirty) {
		super(k, v, dirty);
	}

	@Override
	public boolean update() {
		count++;
		return true;
	}

	@Override
	public int compareTo(Container o) {
		return ((MRUContainer) o).count.compareTo(this.count);
	}
}
