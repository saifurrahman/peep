/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package peep;

import java.awt.GraphicsDevice;
import java.awt.Point;

import processing.core.PApplet;
import tobii.APIException;
import tobii.Configuration;
import tobii.EyeTracker;
import tobii.GazeEvent;
import tobii.GazeListener;
import tobii.MouseTracker;
import tobii.Tracker;
import tobii.filter.Filter;
import tobii.util.V2;

/**
 * Entry point into the PEEP library.
 * 
 * @example PEEP
 * 
 */
public class PEEP implements GazeListener {
	/** The current library version */
	public final static String VERSION = "##library.prettyVersion##";

	/** Link to parent */
	protected final PApplet processing;

	/** The current filter for the raw gaze data */
	protected Filter filter;

	/** The last lowlevel gaze event */
	private volatile GazeEvent lowlevelGazeEvent = null;

	/** The current filter for the raw gaze data */
	private volatile Raw raw;

	/** The current tracking device */
	private Tracker tracker;

	/**
	 * The ID of the tracker (e.g., "mouse", "auto",
	 * "tet-tcp://REXDL-010103004843.local."
	 */
	private String trackerID;

	/** Eyes to use */
	private Eyes eyes = Eyes.BOTH;

	public PEEP filter(Filter filter) {
		this.filter = filter;

		gazeEvent(null);

		return this;
	}
	
	/**
	 * Sets the eyes to use. 
	 * @param eyes
	 * @return
	 */
	public PEEP eyes(Eyes eyes) {
		this.eyes = eyes;
		return this;
	}

	/**
	 * Creates a new eye tracking connection.
	 * 
	 * @example PEEP
	 * @param theParent Parent sketch containing this connection.
	 */
	public PEEP(PApplet theParent) {
		this(theParent, "auto", null);
	}

	/**
	 * Creates a new eye tracking connection.
	 * 
	 * @example PEEP 
	 * @param theParent Parent sketch containing this connection.
	 * @param trackerID USB-ID or URL to connect to.
	 *  
	 */
	public PEEP(PApplet theParent, String trackerID) {
		this(theParent, trackerID, null);
	}
	
	/**
	 * Creates a new eye tracking connection.
	 * 
	 * @example PEEP
	 * @param theParent	Parent sketch containing this connection.
	 * @param trackerID USB-ID or URL to connect to 
	 * @param cheatCode Cheat code :) 
	 *            	 */
	public PEEP(PApplet theParent, String trackerID, String cheatCode) {
		this.processing = theParent;
		this.trackerID = trackerID == null ? "auto" : trackerID;

		filter(Filter.MEDIAN);

		initalizeTracking(cheatCode);
	}

	/** Terminate the old tracker if possible */
	private void terminateOldTracker() {
		if (this.tracker != null) {
			try {
				this.tracker.deregister(this).stop().disconnect();
			} catch (APIException e) {
				exception(e);
			}
		}
	}

	/** Start using the mouse tracker */
	private void useMouseTracker() {
		terminateOldTracker();

		try {
			this.tracker = new MouseTracker();
			this.tracker.register(this).connect().start();
		} catch (APIException e) {
			exception(e);
		}
	}

	/** Initialize eye tracking 
	 * @param cheatCode */
	private void initalizeTracking(String cheatCode) {
		terminateOldTracker();
		
		Configuration configuration = new Configuration();
		try {
			configuration.init();	
		} catch (APIException e) {
			System.err.println("Error using config. Need to pass explicit tracker URL.");
		}		
		
		if ("mouse".equals(this.trackerID)) {
			useMouseTracker();
		} else if ("auto".equals(this.trackerID)) {
			// In auto, first try to connect, then fall back to manual mode
			try {
				this.tracker = new EyeTracker(configuration).cheatcode(cheatCode);
				this.tracker.register(this).connect().start();
			} catch (APIException e) {
				exception(e);
				useMouseTracker();
			}
		} else {
			// In this case we assume we have a true tracker URL.
			try {
				this.tracker = new EyeTracker(configuration, this.trackerID).cheatcode(cheatCode);
				this.tracker.register(this).connect().start();
			} catch (APIException e) {
				exception(e);
			}
		}
	}

	protected void exception(APIException e) {
		System.err.println("API EXCEPTION");
		System.err.println(e);
	}

	/**
	 * Returns the last gaze event.
	 * 
	 * @return The last event or a dummy if there was none.
	 */
	public Raw raw() {
		return this.raw;
	}

	/**
	 * Returns the last, unfiltered, unprocessed low level gaze event as
	 * reported from the tracking device.
	 * 
	 * @return The last event, or <code>null</code> if none was recorded so far.
	 */
	public GazeEvent lowlevel() {
		return this.lowlevelGazeEvent;
	}

	protected V2 rel2pixel(V2 rel) {
		try {
			final GraphicsDevice device = this.processing.frame.getGraphicsConfiguration().getDevice();
			final int x = (int) (rel.x() * device.getDisplayMode().getWidth());
			final int y = (int) (rel.y() * device.getDisplayMode().getHeight());
			return new V2(x, y);
		} catch (Exception e) {
			System.err.println("Error converting the eye tracker position to document coordinates.");
			e.printStackTrace();
			return new V2(-1, -1);
		}
	}

	protected V2 screenpx2window(V2 rel) {
		try {
			final Point bounds = this.processing.getLocationOnScreen();

			int x = (int) (rel.x() - bounds.x);
			int y = (int) (rel.y() - bounds.y);

			return new V2(x, y);
		} catch (Exception e) {
			System.err.println("Error getting the processing location on the screen.");
			e.printStackTrace();
			return new V2(-1, -1);
		}
	}

	@Override
	public void gazeEvent(GazeEvent event) {
		if (event == null) {
			this.raw = Raw.empty(this);
			return;
		}

		this.lowlevelGazeEvent = event;

		// Create the Raw object based on the set eye to use.
		V2 eyes = event.center().gazeOnDisplayNorm;		
		
		if (this.eyes.equals(Eyes.LEFT)) {
			eyes = event.left.gazeOnDisplayNorm;
		}
		
		if (this.eyes.equals(Eyes.RIGHT)) {
			eyes = event.right.gazeOnDisplayNorm;
		}
		
		this.raw = new Raw(this, eyes, event.nanoTimeReceived / 1000000);
	}

	@Override
	public void apiException(APIException exception) {
		exception(exception);
	}
}
