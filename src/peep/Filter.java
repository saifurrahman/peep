package peep;

import java.util.LinkedList;
import java.util.List;

import tobii.util.Vn;

public abstract class Filter {
	protected Filter() {}
	
	public static Filter NONE = new Filter() {
		@Override
		public Vn filter(Vn in) {
			return in;
		}		
	};
	
	public static Filter MEDIAN = new Filter() {
		private final int N = 3; 
		private List<Vn> past = new LinkedList<Vn>();
		
		@Override
		public Vn filter(Vn in) {
			past.add(in);
			
			if(past.size() > N) 
				past.remove(0);
			
			if(past.size() < N) 
				return in;
			
			double x = Vn.row(past, 0).median();
			double y = Vn.row(past, 1).median();
			
			return new Vn(x, y);
		}		
	};
	

	/** 
	 * Called to filter some vector.
	 * 
	 * @param in The vector to filter.
	 * @return A filtered vector.
	 */
	public abstract Vn filter(Vn in);
}
