import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MRUContainerSetTest {

    @Test 
	public void test6Inserts() {
		ContainerSet<String, Integer> cs = new MRUContainerSet(4);

		cs.insert("one", 1, false, new NoDataStore());
		assertEquals(cs.size(), 1);

		cs.insert("two", 2, false, new NoDataStore());
		assertEquals(cs.size(), 2);

		cs.insert("three", 3, false, new NoDataStore());
		assertEquals(cs.size(), 3);
		assertEquals(cs.evict(), -1);

		cs.insert("four", 4, false, new NoDataStore());
		assertEquals(cs.size(), 4);
		assertEquals(cs.evict(), 3);

		cs.insert("five", 5, false, new NoDataStore());
		assertEquals(cs.size(), 4);
		assertNull(cs.get("four"));
		assertThat(cs.get("three"), is(3));
		assertThat(cs.get("one"), is(1));

		assertThat(cs.evict(), is(0));
		cs.insert("nine", 9, true, new NoDataStore());

		assertNull(cs.get("one"));
		System.out.println(cs);
		assertThat(cs.getDirtyElements().size(), is(1));
    }
}
