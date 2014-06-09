package enemyClases;
import utils.Damage;

/**
 * Implements the Enemy Class Soldier, with 20 Base HP, 3 Base Attack Points, 
 * and special Attack ability of 2x multiplier if the player last action was running
 * @author teohoch
 *
 */
public class Soldier implements EnemyClass {


	@Override
	public int getHP() {
		return 20;
	}

	@Override
	public int getAtackPoints() {
		return 3;
	}

	@Override
	public int getAtackAbility(String LastCommand) {
		if(LastCommand.contains("correr"))
		{
			return 2;
		}
		return 1;
	}

	@Override
	public double getDefenceAbility(Damage damage) {
		return 1;
	}

	@Override
	public String getName() {
		return "Soldado";
	}

}
