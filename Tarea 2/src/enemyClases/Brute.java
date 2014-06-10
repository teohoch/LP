package enemyClases;
import utils.Damage;

/**
 * Implements the Enemy Class Brute, with 30 Base HP, 5 Base Attack Points, 
 * and special Defence ability of 0.5x multiplier if the damage is from a Direct Attack, 
 * and 2x if the damage is from an offensive object.
 * @author teohoch
 *
 */
public class Brute implements EnemyClass {

	@Override
	public int getHP() {
		return 30;
	}

	@Override
	public int getAtackPoints() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getAtackAbility(String LastCommand) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getDefenceAbility(Damage damage) {
		switch (damage.getType()) {
		case 'D':
			return 0.5;
		case 'O':
			return 2;
		default:
			return 1;
		}
	}

	@Override
	public String getName() {
		return "Bruto";
	}

}
