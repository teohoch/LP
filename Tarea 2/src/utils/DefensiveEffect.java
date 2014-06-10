package utils;
/**
 * Clase contenedora, construida para guardar los effectos de los items defensivos. 
 * Incluye el origen, el valor defensivo, el tiempo maximo y el tiempo restante del efecto.
 * @author teohoch
 *
 */
public class DefensiveEffect {
	private String origin;
	private int val;
	private int maxTurns;
	private int remainingTurns;
	/**
	 * @param origin	Origen del efecto defensivo (ej. Nombre del objeto protector)
	 * @param maxTurns	Tiempo maximo del efecto
	 * @param val		Valor del efecto de proteccion
	 */
	public DefensiveEffect(String origin, int maxTurns, int val) {
		super();
		this.origin = origin;
		this.maxTurns = maxTurns;
		this.remainingTurns = this.maxTurns;
		this.val = val;
	}
	/**
	 * Implementa el paso de un turno en el efecto.
	 */
	public void turnPassed()
	{
		remainingTurns = remainingTurns -1;
	}
	public String getOrigin() {
		return origin;
	}
	public int getMaxTurns() {
		return maxTurns;
	}
	public int getRemainingTurns() {
		return remainingTurns;
	}
	public int getVal() {
		return val;
	}
	

}
