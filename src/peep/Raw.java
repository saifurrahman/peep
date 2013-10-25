package peep;

import java.awt.Point;

import tobii.GazeEvent;
import tobii.util.V2;


/**
 * Represents a single gaze measurement. 
 * 
 * @author Ralf Biedert <rb@xr.io>
 */
public class Raw extends ExchangeableGazeEntity {
	
	protected Raw(PEEP peep, V2 rel, long time, boolean valid) {
		super(peep);
		
		// Only filter on valid data
		V2 filtered;
		
		if(valid) {
			filtered = peep.filter.filter(rel).v2();
		} else {
			filtered = rel.v2();
		}
		 
		V2 docpos = peep.screenpx2window(peep.rel2pixel(filtered));
		
		this.x = (int) docpos.x();
		this.y = (int) docpos.y();		
		this.time = time; 
		this.valid = valid;
	}
	
	public static Raw empty(PEEP peep) {
		return new Raw(peep, new V2(-1, -1), System.nanoTime() / 1000000, false);
	}
	
		
	/** The x position on the processing window */
	public final int x;

	/** The y position on the processing window */
	public final int y;
	
	/** If this measurement is a valid measurement. */
	public final boolean valid;
	
	/** The relative time in ms of this event */
	public final long time;
	
	/**
	 * Return true if the user's gaze is on the sketch.
	 * @return
	 */
	public boolean onSketch() {
		return peep.processing.frame.getBounds().contains(new Point(x, y));
	}
}
