package enemyClases;
import utils.Damage;

/**
 * Implements the Enemy Class Warlock, with 10 Base HP, 5 Base Attack Points, 
 * and special Attack ability of 2x multiplier if the player last action was running
 * @author teohoch
 *
 */
public class Warlock implements EnemyClass {

	@Override
	public int getHP() {
		return 10;
	}

	@Override
	public int getAtackPoints() {
		return 5;
	}

	@Override
	public int getAtackAbility(String LastCommand) {
		return 1;
	}

	@Override
	public double getDefenceAbility(Damage damage) {
		switch (damage.getType()) {
		case 'd':
			return 2;
		case 'o':
			return 0.5;
		default:
			return 1;
		}
	}

	@Override
	public String getName() {
		return "Hechicero";
	}

}
