import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LRUContainerSetTest {

    @Test 
	public void test6Inserts() {
		ContainerSet<String, Integer> cs = new LRUContainerSet(4);

		cs.insert("one", 1, false);
		assertEquals(cs.size(), 1);

		cs.insert("two", 2, false);
		assertEquals(cs.size(), 2);

		cs.insert("three", 3, false);
		assertEquals(cs.size(), 3);
		assertEquals(cs.evict(), -1);

		cs.insert("four", 4, false);
		assertEquals(cs.size(), 4);
		assertEquals(cs.evict(), 0);

		cs.insert("five", 5, false);
		assertEquals(cs.size(), 4);
		assertNull(cs.get("one"));
		assertThat(cs.get("three"), is(3));
		assertThat(cs.get("four"), is(4));

		System.out.println(cs);
		assertThat(cs.evict(), is(1));
		cs.insert("nine", 9, true);

		assertNull(cs.get("two"));
		System.out.println(cs);
		assertThat(cs.getDirtyElements().size(), is(1));
    }
}
