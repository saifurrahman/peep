package peep;

import java.awt.Point;

import tobii.GazeEvent;
import tobii.util.V2;


/**
 * Represents a single gaze measurement. 
 * 
 * @author Ralf Biedert <rb@xr.io>
 */
public class Gaze extends ExchangeableGazeEntity {
	
	protected Gaze(PEEP peep, GazeEvent raw) {
		// TODO: Change to raw.center() ...
		this(peep, raw.left.gazeOnDisplayNorm);
	}
	
	protected Gaze(PEEP peep, V2 rel) {
		super(peep);
		
		// TODO: Change to raw.center() ...
		V2 filtered = peep.filter.filter(rel).v2();
		V2 docpos = peep.screenpx2window(peep.rel2pixel(filtered));
		
		x = (int) docpos.x();
		y = (int) docpos.y();
	}
	
	
	/** The x position on the processing window */
	public final int x;

	/** The y position on the processing window */
	public final int y;
	
	/**
	 * Return true if the user's gaze is on the sketch.
	 * @return
	 */
	public boolean onSketch() {
		return peep.processing.frame.getBounds().contains(new Point(x, y));
	}
}
