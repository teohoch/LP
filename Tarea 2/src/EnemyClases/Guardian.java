package EnemyClases;
import utils.Damage;

/**
 * Implements the Enemy Class Guardian, with 15 Base HP, 2 Base Attack Points, 
 * and special Attack ability of 2x multiplier if the player last action was picking up a item.
 * @author teohoch
 *
 */
public class Guardian implements EnemyClass {

	@Override
	public int getHP() {
		return 15;
	}

	@Override
	public int getAtackPoints() {
		return 2;
	}

	@Override
	public int getAtackAbility(String LastCommand) {
		if(LastCommand.contains("tomar"))
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
		return "Guardian";
	}

}
