package utils;
/**
 * Container class for damage. Contains the value and type of it
 */

/**
 * @author teohoch
 *
 */
public class Damage {
	
	private final int value;
	private final char type;
	
	/**
	 * Initializes the object with the parameters given
	 * @param value The Value of the damage
	 * @param type The type of damage. 'd' is direct damage and 'o' is object damage
	 */
	public Damage(int value, char type)
	{
		this.value = value;
		this.type = type;
	}
	/**
	 * 
	 * @return Returns the Damage Value.
	 */
	public int getValue()
	{
		return value;
	}
	/**
	 * 
	 * @return Returns the Damage Type.
	 */
	public char getType()
	{
		return type;
	}

}
