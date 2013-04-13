package peep;

import tobii.util.Vn;

public abstract class Filter {
	protected Filter() {}
	
	public static Filter NONE = new Filter() {
		@Override
		public Vn filter(Vn in) {
			return in;
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
