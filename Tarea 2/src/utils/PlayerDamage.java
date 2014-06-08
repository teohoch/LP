/**
 * 
 */
package utils;

/**
 * @author teohoch
 *Daño generado por el Jugador
 */
public class PlayerDamage extends Damage {

	private final String weaponOrigin;
	/**
	 * Inicializa el objeto
	 * @param value El valor del daño
	 * @param type El tipo de Daño. 'd' es daño directo y 'o' daño de objeto
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
