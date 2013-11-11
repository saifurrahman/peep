package peep;

import java.awt.Point;

import tobii.GazeEvent;
import tobii.util.V2;


/**
 * Represents a single gaze measurement. 
 * 
 * @author Ralf Biedert <rb@xr.io>
 */
public class CombinedRaw extends AbstractRaw {

	protected static V2 xy(PEEP peep, boolean valid, GazeEvent event) {
		
		// Create the Raw object based on the set eye to use.		
		V2 eyes = event.center().gazeOnDisplayNorm;
				
		if (peep.eyes().equals(Eyes.LEFT)) {
			eyes = event.left.gazeOnDisplayNorm;
		}
		
		if (peep.eyes().equals(Eyes.RIGHT)) {
			eyes = event.right.gazeOnDisplayNorm;
		}		

		// Only filter on valid data
		final V2 filtered = valid ? peep.filter.filter(eyes).v2() : eyes.v2(); 			
		return peep.screenpx2window(peep.rel2pixel(filtered));
	}
	
	protected static boolean validity(PEEP peep, GazeEvent event) {		
		/*
	  		TOBIIGAZE_TRACKING_STATUS_NO_EYES_TRACKED(0),
			TOBIIGAZE_TRACKING_STATUS_BOTH_EYES_TRACKED(1),
			TOBIIGAZE_TRACKING_STATUS_ONLY_LEFT_EYE_TRACKED(2),
			TOBIIGAZE_TRACKING_STATUS_ONE_EYE_TRACKED_PROBABLY_LEFT(3),
			TOBIIGAZE_TRACKING_STATUS_ONE_EYE_TRACKED_UNKNOWN_WHICH(4),
			TOBIIGAZE_TRACKING_STATUS_ONE_EYE_TRACKED_PROBABLY_RIGHT(5),
			TOBIIGAZE_TRACKING_STATUS_ONLY_RIGHT_EYE_TRACKED(6);
		*/		
		
		switch(peep.eyes()){
			case BOTH: return event.status > 0;		
			case LEFT: return event.status == 1 || event.status == 2 || event.status == 3; 		
			case RIGHT: return event.status == 1 || event.status == 6 || event.status == 5;
			default: return false;
		}
	}
	
	protected CombinedRaw(PEEP peep, GazeEvent event) {
		// Java, you suck.
		super(peep, event, xy(peep, validity(peep, event), event), validity(peep, event));		
	}
	
	protected CombinedRaw(PEEP peep) {
		super(peep, null, null, false);	
	}

	protected static CombinedRaw empty(PEEP peep) {
		return new CombinedRaw(peep);
	}
			
	public SingleEyeRaw unfilteredleft() {		
		if (event == null) {
			return new SingleEyeRaw(peep, this.event, new V2(-1, -1), false);			
		}
		
		final boolean validity = event.status == 1 || event.status == 2 || event.status == 3; 
		final V2 eyes = event.left.gazeOnDisplayNorm.v2();				
		final V2 pos = peep.screenpx2window(peep.rel2pixel(eyes));
		
		return new SingleEyeRaw(peep, this.event, pos, validity);
	}
	
	public SingleEyeRaw unfilteredright() {		
		if (event == null) {
			return new SingleEyeRaw(peep, this.event, new V2(-1, -1), false);			
		}
		
		final boolean validity = event.status == 1 || event.status == 5 || event.status == 6; 
		final V2 eyes = event.right.gazeOnDisplayNorm.v2();				
		final V2 pos = peep.screenpx2window(peep.rel2pixel(eyes));
		
		return new SingleEyeRaw(peep, this.event, pos, validity);
	}
		
	/**
	 * Return true if the user's gaze is on the sketch.
	 * 
	 * @return
	 */
	public boolean onSketch() {
		return peep.processing.frame.getBounds().contains(new Point(x, y));
	}
}
