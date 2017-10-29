import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

public class CacheTest implements ResultShare<String, Integer> {
	ArrayList<String> puts = new ArrayList<String>();
	ArrayList<String> gets = new ArrayList<String>();
	
	class TestDataStore implements DataStore<String, Integer> {
		ResultShare<String, Integer> rs;
		TestDataStore(ResultShare<String, Integer> rs) {
			this.rs = rs;
		}
		
		@Override
		public Integer get(String key) {
			rs.notifyGet(key);
			switch(key) {
				case "one": return 1;
				case "two": return 2;
				case "three": return 3;
				case "four": return 4;
				case "five": return 5;
				case "six": return 6;
				case "seven": return 7;
				case "eight": return 8;
				case "nine": return 9; 
				case "ten": return 10;
				default: return null;
			}
		}

		@Override
		public void put(String key, Integer value) {
			rs.notifyPut(key, value);
		}
	}

    @Test 
	public void testBasicCache() {
		TestDataStore testDS = new TestDataStore(this);
		Cache<String, Integer> cache = new Cache(3, 4, testDS);
		cache.get("one");
		assertThat(gets.get(0), is("one"));
		cache.get("two");
		assertThat(gets.get(1), is("two"));
		assertThat(gets.size(), is(2));

		assertThat(cache.get("two"), is(2));
		assertThat(gets.size(), is(2));

		cache.put("fourteen", 14);
		assertThat(puts.size(), is(0));
		cache.writeAllToDataStore();
		assertThat(puts.size(), is(1));
		cache.put("one", 2);

		cache.writeAllToDataStore();
		assertThat(puts.size(), is(3));
	}

	@Test
	public void testCustomReplacementPolicy() {
		try { 
			Cache<String, Integer> cache = new Cache(BadReplacementPolicy.class, 3, 2, new NoDataStore());
			cache.put("one", 1);
			cache.put("two", 2);
			cache.put("three", 3);

			assertThat(cache.get("one"), is(1));
			cache.put("four", 4);
			cache.put("five", 5);
			cache.put("six", 6);
			assertNull(cache.get("one"));
			System.out.println(cache);
			cache.put("one", 1);
			System.out.println(cache);
			assertNull(cache.get("two"));
			System.out.println(cache);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyPut(String k, Integer v) {
		puts.add(k);
	}

	@Override
	public void notifyGet(String k) {
		gets.add(k);
	}
}
