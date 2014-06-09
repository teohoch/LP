package enemyClases;
import utils.Damage;


/**
 * Interface for Different classes of enemies.
 * @author teohoch
 *
 */
public interface EnemyClass {
	/**
	 * 
	 * @return Devuelve el Nombre de esta clase.
	 */
	public String getName();
	/**
	 * @return Devuelve el HP base para esta clase.
	 */
	public int getHP();
	/**
	 * @return Devuelve los puntos de impacto base para esta clase.
	 */
	public int getAtackPoints();
	/**
	 * 
	 * @param LastCommand La ultima accion realizada por el usuario
	 * @return Returns Devuelve el multiplicador asociado a la habilidad de ataque.
	 */
	public int getAtackAbility(String LastCommand);
	/**
	 * 
	 * @param damage Contenedor del daño.
	 * @return Devuelve el multiplicador asociado a la habilidad de Defensa.
	 */
	public double getDefenceAbility(Damage damage);

}
