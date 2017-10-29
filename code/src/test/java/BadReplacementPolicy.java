public class BadReplacementPolicy<K, V> extends ContainerSet<K, V> {
	
	private int replace = 0;

	public BadReplacementPolicy(Integer slots) {
		super(slots);
	}

	@Override
	public int evict() {
		if (size() < getContainers().length) 
			return -1;

		return replace++;
	}
}
