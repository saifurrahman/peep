package sandbox;

import peep.PEEP;
import tobii.filter.Filter;

public class SimpleTest {
	public static void main(String[] args) throws InterruptedException {
		PEEP peep = new PEEP(null);
		
		peep.lowlevel();
		peep.filter(Filter.NONE);
		
		while(true) {
			Thread.sleep(50);
			
			if(!peep.raw().changed()) continue;
			
			int t = (int) peep.raw().time;
			int x = peep.raw().x;
			int y = peep.raw().y;
			
			System.out.println(t + " " + x + " " + y);
		}
		
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
