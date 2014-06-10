package utils;
/**
 * Clase contenedor para daños. Contiene el valor y tipo del daño, ademas de su procedencia.
 * Existe 2 tipos de daño "D" o Directo, y por objeto ofensivos o "O" 
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
	 * @param type El tipo de Daño. 'D' es daño directo y 'O' daño de objeto
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
