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
	private final String origin;
	
	/**
	 * Inicializa el objeto
	 * @param value El valor del daño
	 * @param type El tipo de Daño. 'd' es daño directo y 'o' daño de objeto
	 * @param origin El nombre del origen del daño (nombre jugador/monstruo)
	 */
	public Damage(int value, char type, String origin)
	{
		this.value = value;
		this.type = type;
		this.origin = origin;
	}
	/**
	 * 
	 * @return retorna el valor del daño
	 */
	public int getValue()
	{
		return value;
	}
	/**
	 * 
	 * @return Retorna el tipo de daño
	 */
	public char getType()
	{
		return type;
	}
	/**
	 * 
	 * @return Retorna el Nombre del originario del daño
	 */
	public String getOrigin() {
		return origin;
	}

}
