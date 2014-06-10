/**
 * 
 */
package utils;

/**
 * Daño generado por el Jugador, contiene espacio para conter el nombre del arma u objeto utilizado.
 * @author teohoch
 */
public class PlayerDamage extends Damage {

	private final String weaponOrigin;
	/**
	 * Inicializa el objeto
	 * @param value El valor del daño
	 * @param type El tipo de Daño. 'D' es daño directo y 'O' daño de objeto
	 * @param origin El nombre del origen del daño (nombre jugador/monstruo)
	 * @param weaponOrigin El nombre del arma/objeto que origino el daño
	 */
	public PlayerDamage(int value, char type, String origin,String weaponOrigin) {
		super(value, type, origin);
		this.weaponOrigin = weaponOrigin;
	}
	/**
	 * @return the weaponOrigin
	 */
	public String getWeaponOrigin() {
		return weaponOrigin;
	}

}
