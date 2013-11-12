package peep;

import tobii.GazeEvent;
import tobii.util.V2;

public abstract class AbstractRaw extends ExchangeableGazeEntity {
	
	/** The gaze event from which we got our data from. */
	protected final GazeEvent event;
		
	/** The x position on the processing window */
	public final int x;

	/** The y position on the processing window */
	public final int y;
	
	/** If this measurement is a valid measurement. */
	public final boolean valid;
	
	/** The relative time in ms of this event */
	public final long time;
	
	protected AbstractRaw(PEEP peep, GazeEvent event, V2 xy, boolean valid) {
		super(peep);
				
		this.time = System.nanoTime() / 1000000; 
		this.event = event;
		this.valid = valid;
		
		if (xy != null) {
			this.x = (int) xy.x();
			this.y = (int) xy.y();		
		} else {
			this.x = -1;
			this.y = -1;
		}	
	}
}
