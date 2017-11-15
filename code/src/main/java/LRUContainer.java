/**
 * @author Parth Mehrotra <parth@mehrotra.me>
 *
 * A ContainerSet that models the LRU Replacement Policy
 */
public class LRUContainer<K, V> extends Container<K, V> {

	public Long count = 0L;

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
