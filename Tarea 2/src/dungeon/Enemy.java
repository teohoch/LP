package dungeon;
import java.awt.Point;

import utils.Damage;
import EnemyClases.Brute;
import EnemyClases.EnemyClass;
import EnemyClases.Guardian;
import EnemyClases.Soldier;
import EnemyClases.Warlock;
import cl.utfsm.inf.lp.sem12014.mud.logic.exceptions.GameLogicException;
import cl.utfsm.inf.lp.sem12014.mud.logic.interfaces.BonusEnemyInterface;

/**
 * Implements the Bonus Enemy Interface, with support for different classes of enemies.
 */

/**
 * @author teohoch
 *
 */
public class Enemy implements BonusEnemyInterface {
	
	private String name;
	private int modifHP;
	private int modifAttackPoints;
	private int currentHP;
	private int currentAttackPoints;
	private int XP;
	private Point location;
	private char classId;
	private EnemyClass type;	
	
	private void init()
	{
		/*if (Type != null) 
		{
			currentHP = Type.getHP() + modifHP;
			currentAtackPoints = Type.getAtackPoints() + modifAttackPoints;						
		} */
	}

	
	@Override
	public void setAttackPoints(int arg0) throws GameLogicException {
		modifAttackPoints = arg0;
	}

	
	@Override
	public void setExpPoints(int arg0) throws GameLogicException {
		XP = arg0;
	}

	
	@Override
	public void setHitPoints(int arg0) throws GameLogicException {
		modifHP = arg0;
	}

	
	@Override
	public void setName(String arg0) throws GameLogicException {
		name = arg0;
	}

	
	@Override
	public void setPosition(Point arg0) throws GameLogicException {
		location = arg0;
		
		init();
	}

	
	@Override
	public void setType(char arg0) throws GameLogicException {
		classId = arg0;
		switch (arg0) {
		case 'S':
			type = new Soldier();			
			break;
		case 'G':
			type = new Guardian();
			break;
		case 'B':
			type = new Brute();
			break;
		case 'W':
			type = new Warlock();
			break;
		}

	}
	
	public Damage Attack()
	{
		return (new Damage(currentAttackPoints,'D'));
	}
	
	public void Defend(Damage damage)
	{
		int efectiveDamage = (int) (damage.getValue() * type.getDefenceAbility(damage));
		currentHP = currentHP - efectiveDamage;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the currentAtackPoints
	 */
	public int getCurrentAtackPoints() {
		return currentAttackPoints;
	}


	/**
	 * @return the currentHP
	 */
	public int getCurrentHP() {
		return currentHP;
	}


	/**
	 * @return the xP
	 */
	public int getXP() {
		return XP;
	}
	
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @return the classId
	 */
	public char getClassId() {
		return classId;
	}
	
	public void println()
	{
		System.out.println(name +" " 
				+ type.getName() +" " + modifHP +" " + modifAttackPoints 
				+" " + XP +" " + location.x +" " + location.y);
	}







}
