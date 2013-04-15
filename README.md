

# PEEP 3.0 #

The [Processing](http://processing.org) Easy Eye-Tracking Plugin.

  * Access your gaze data with 3 lines of code 
  * Supports most modern Tobii eye trackers (REX, X120, ...) and mouse emulation
  * Runs with Processing 2.0 and [Processing.py](https://github.com/jdf/processing.py)
  
Please note, PEEP 3.0 requires Windows and a 64 bit JVM at the moment.


## Quickstart ##

Download the __[latest release of the Java Bindings](http://s.xr.io/peep/latest.zip)__ and place it into Processing's `libraries` folder. If you need help see the section [Manual Install in this document](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).

  
Paste this code into your  [Processing](http://processing.org) sketch:

	import PEEP.*;

	PEEP peep;

	void setup() {
	  peep = new PEEP(this);
	}

	void draw() {
	  ellipse(peep.raw().x, peep.raw().y, 10, 10);  
	}

 
Or, if you are using [Processing.py](https://github.com/jdf/processing.py):
  
	import peep.PEEP as PEEP
	
	peep = None

	def setup():
	  global peep
	  peep = PEEP(this)
	
	def draw():
	  ellipse(peep.raw().x, peep.raw().y, 10, 10)




## News / Changelog ##

  * (2013/04/15) -- Initial release, core features working.



## FAQ ##

  * __Does it work with 32bit JVMs?__

  Not at the moment. Please update to a 64bit JVM.


## License ##

Read this carefully:

The actual __JAR library__ and its sources are licensed as [LGPL 2.1](http://www.gnu.org/licenses/lgpl-2.1.html). 

The underlying Tobii Gaze SDK is licensed according to the [Tobii Gaze SDK Agreement](http://www.tobii.com/gaze-interaction/global/products-services/tobii-gaze-sdk/). In particular it does _not allow for developing applications that use eye tracking data for recording behavior_.



## Credits ##

  * PEEP created by [Ralf Biedert](http://xr.io)

Uses code from [Tobii Technology AB](http://tobii.com) and [Bridj](https://code.google.com/p/bridj/).

