package peep;

import tobii.GazeEvent;
import tobii.util.V2;

public class SingleEyeRaw extends AbstractRaw {

	protected SingleEyeRaw(PEEP peep, GazeEvent event, V2 pos, boolean validity) {
		super(peep, event, pos, validity);
	}

}
