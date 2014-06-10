package dungeon;
import java.awt.Point;

import utils.Damage;
import utils.PlayerDamage;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader.MessageCode;
import cl.utfsm.inf.lp.sem12014.mud.logic.exceptions.GameLogicException;
import cl.utfsm.inf.lp.sem12014.mud.logic.interfaces.BonusEnemyInterface;
import enemyClases.Brute;
import enemyClases.EnemyClass;
import enemyClases.Guardian;
import enemyClases.Soldier;
import enemyClases.Warlock;

/**
 * Implementa la Interfaz Bonus Enemy, con soporte para 4 clases de enemigos.
 */

/**
 * @author teohoch
 *
 */
public class Enemy implements BonusEnemyInterface {
	
	private String name;
	private int baseHP;
	private int baseAttackPoints;
	private int currentHP;
	private int currentAttackPoints;
	private int XP;
	private Point location;
	private char classId;
	private EnemyClass type;		
	
	private boolean attacked;
	private MessageReader messenger;
	
	public void init(MessageReader messenger)
	{
		currentHP = type.getHP() + baseHP;
		currentAttackPoints = type.getAtackPoints() + baseAttackPoints;	
		this.messenger = messenger;

	}

	
	@Override
	public void setAttackPoints(int arg0) throws GameLogicException {
		baseAttackPoints = arg0;
	}

	
	@Override
	public void setExpPoints(int arg0) throws GameLogicException {
		XP = arg0;
	}

	
	@Override
	public void setHitPoints(int arg0) throws GameLogicException {
		baseHP = arg0;
	}

	
	@Override
	public void setName(String arg0) throws GameLogicException {
		name = arg0;
	}

	
	@Override
	public void setPosition(Point arg0) throws GameLogicException {
		location = arg0;		
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
	/**
	 * Genera el daño que el enemigo realiza, a partir de el daño base, el daño de clase y la habilidad de clase
	 * @param lastCommand Es el ultimo comando ingresado por el usuario
	 * @return Retorna el el daño generado por el enemigo
	 */
	public Damage Attack(String lastCommand)
	{
		int mult = type.getAtackAbility(lastCommand);
		attacked = false;
		return (new Damage(currentAttackPoints*mult,'D',this.getName()));
	}
	/**
	 * Recive el ataque y, calcula y aplica el daño efectivo al enemigo a partir de su habilidad de clase
	 * @param damage objeto que contiene al daño generado por el jugador
	 */
	public void Defend(PlayerDamage damage)
	{
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_PLAYER)+"\n",damage.getOrigin(),damage.getWeaponOrigin(),this.getName());
		int efectiveDamage = (int) (damage.getValue() * type.getDefenceAbility(damage));
		currentHP = currentHP - efectiveDamage;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_DAMAGE)+"\n", this.getName(),efectiveDamage);
		attacked = true;
	}
	/**
	 * Retorna si el enemigo esta muerto o no
	 * @return muerto => true, vivo =>false
	 */
	public boolean isDead() {
		return (currentHP<=0);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name +" "+ type.getName();
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
	/**
	 * 
	 * @return true si fue atacado
	 */
	public boolean isAttacked() {
		return attacked;
	}

	/**
	 * Setea si el enemigo fue atacado o no.
	 * @param attacked
	 */
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}


	







}
