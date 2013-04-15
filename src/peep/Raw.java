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
	
	protected Raw(PEEP peep, GazeEvent raw) {
		this(peep, raw.center().gazeOnDisplayNorm, raw.nanoTimeReceived / 1000000);
	}
	
	protected Raw(PEEP peep, V2 rel, long time) {
		super(peep);
		
		V2 filtered = peep.filter.filter(rel).v2();
		V2 docpos = peep.screenpx2window(peep.rel2pixel(filtered));
		
		this.x = (int) docpos.x();
		this.y = (int) docpos.y();		
		this.time = time; 
	}
	
	public static Raw empty(PEEP peep) {
		return new Raw(peep, new V2(-1, -1), System.nanoTime() / 1000000);
	}
	
		
	/** The x position on the processing window */
	public final int x;

	/** The y position on the processing window */
	public final int y;

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
