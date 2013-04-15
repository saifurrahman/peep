
# Import the library
import peep.PEEP as PEEP

# Define our global PEEP object
peep = None


def setup():
	global peep # needed because we assign it here

	size(800, 600, P3D)

	# connect to the next best tracker, or fall back to mouse
	peep = PEEP(this)


def draw():
	# draw an ellipse at the recorded gaze position	
	ellipse(peep.raw().x, peep.raw().y, 10, 10)
