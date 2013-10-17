package peep;

import static org.junit.Assert.*;

import org.junit.Test;

import tobii.filter.Filter;
import tobii.util.V2;
import tobii.util.V3;
import tobii.util.Vn;

public class FilterTest {
	
	@Test
	public void testMedian() {
						
		Filter m = Filter.MEDIAN;
		m.filter(new Vn(1, 4));
		m.filter(new Vn(3, 6));
		Vn res = m.filter(new Vn(2, 0));
		
		System.out.println(res);
		assertEquals(2, res.get(0), 0.001);
		assertEquals(4, res.get(1), 0.001);

	}	
}
