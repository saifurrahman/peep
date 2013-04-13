package sandbox;

import peep.Filter;
import peep.PEEP;

public class SimpleTest {
	public static void main(String[] args) {
		PEEP peep = new PEEP(null);
		
		peep.lowlevel();
		peep.filter(Filter.NONE);
		int x = peep.raw().x;
		int y = peep.raw().y;
		peep.raw().changed();
		
		/*
		peep.inFixation()
		peep.inSaccade
		
		peep.fixation.x
		peep.fixation.y
		peep.fixation.changed()
		peep.fixation.in()
		*/
	}
}
