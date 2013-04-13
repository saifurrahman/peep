package peep;

public class ExchangeableGazeEntity {
	/** If the object changed since last query */
	protected boolean changed = true;
	
	/** Parent PEEP object */
	protected final PEEP peep;
	
	/** The time this entity was observed */
	public final long time;

	protected ExchangeableGazeEntity(PEEP peep) {
		this.peep = peep;		
		this.time = ns2ms(System.nanoTime());
	}

	/**
	 * Returns true if the entity changed since the last call to this method.
	 * 
	 * @return True if it changed, false if not.
	 */
	public boolean changed() {
		if (changed) {
			changed = false;
			return true;
		}

		return false;
	}

	protected long ns2ms(long ns) {
		return ns / 1000000;
	}
}
