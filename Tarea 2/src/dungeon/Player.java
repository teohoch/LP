package dungeon;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader.MessageCode;
import cl.utfsm.inf.lp.sem12014.mud.input.PlayerReader;
import utils.Damage;
import utils.DefensiveEffect;
import utils.Item;
import utils.PlayerDamage;

public class Player {
	private String name;
	
	private int maxHP;
	private int currentHP;
	
	private int exp;
	private int level;
	
	int attack;
	int defense;
	
	private Item weapon;
	private Item armor;
	
	private List <Item> inventory;
	private int maxInventory;
	private Point initPosition;
	
	private DefensiveEffect defensiveEffect;
	
	private MessageReader messenger;
	
	public Player(String filePath,MessageReader messenger)
	{
		this.messenger = messenger;
		loadPlayer(filePath);
		defensiveEffect = null;
		exp = 0;
		level = 1;
		maxInventory = 10;
		
	}
	/**
	 * Carga los datos del jugador desde el archivo "player.mud",
	 * tales como el nombre, el hp y posicion inicial y los objetos en el inventario.
	 * @param filePath lugar donde se encuentra el archivo de jugador
	 */
	private void loadPlayer(String filePath)
	{
		PlayerReader pReader = null;
		try {
			pReader = new PlayerReader(filePath);
		} catch (IOException e) {
			System.out.print(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		}
		name = pReader.getName();
		maxHP = currentHP = pReader.getHitPoints();
		initPosition = pReader.getPosition();
		
		
		Map.Entry<String, Integer> armorStats = pReader.getArmor();
		Map.Entry<String, Integer> weaponStats = pReader.getWeapon();
		armor = new Item(armorStats.getKey(),'A',armorStats.getValue(),new Point(-1, -1));
		weapon = new Item(weaponStats.getKey(),'W',weaponStats.getValue(),new Point(-1, -1));
		
		loadInventory(pReader);
		
	}
	/**
	 * Carga los objetos en el inventario del jugador desde el archivo player.mud
	 * @param pReader es el lector de objetos
	 */
	private void loadInventory(PlayerReader pReader) {
		String[] itemsNames = pReader.getKeys();
		List <Item> tempList = new	ArrayList<Item>();
		for (String name : itemsNames) {
			char type = pReader.getType(name);
			int value = pReader.getValue(name);
			Item Temp = new Item(name, type, value, new Point(-1,-1));
			tempList.add(Temp);			
		}		
	}
	/**
	 * Genera el daño por el jugador a partir de su arma
	 * @return el daño por el usuario
	 */
	public Damage Attack()
	{
		return (new PlayerDamage(attack,'D',name,weapon.getName()));
	}
	
	/**
	 * Recive el ataque y, calcula y aplica el daño efectivo al jugador
	 * @param damage Ataque proveniente del enemigo
	 */
	public void Defend(Damage damage)
	{
		int efectiveDamage = (int) (damage.getValue()- defense -defensiveEffect.getVal());
		efectiveDamage = (efectiveDamage > 0) ? efectiveDamage : 0;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_ENEMY),damage.getOrigin(),name,efectiveDamage);
		currentHP = currentHP - efectiveDamage;
	}
	/**
	 * Revisa si el objeto esta en el inventario
	 * @param name Nombre del objeto
	 * @return respuesta a ¿esta el objeto en el inventario?
	 */
	public boolean InInventory(String name)
	{
		boolean isIn = false;
		for (Item item : inventory) {
			if (item.getName()==name) {
				isIn = true;
			}	
		}
		return isIn;
	}
	/**
	 * Busca un objeto dentro del inventario
	 * @param name nombre del objeto
	 * @return objeto buscado, si no existe returna null
	 */
	private Item findItem(String name)
	{
		Item itemFound = null;
		for (Item item : inventory) {
			if (item.getName()==name) {
				itemFound =item;
			}	
		}
		return itemFound;

	}
	/**
	 * Maneja el uso de objetos del inventario
	 * @param itemName
	 * @return
	 */
	public PlayerDamage UseItem(String itemName)
	{
		Item item = findItem(itemName);
		PlayerDamage returning = null;
		switch (item.getType()) {
		case 'H':
			if(currentHP<maxHP)
			{
				int recoverHP;
				if ((currentHP+item.getValue()<maxHP)) {
					currentHP = currentHP+item.getValue();
					recoverHP = item.getValue();
				} else {
					recoverHP = maxHP - currentHP;
					currentHP = maxHP;
				}
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_HEAL),name,item.getName(),recoverHP);
				inventory.remove(item);
				returning = new PlayerDamage(-1, 'n', null, null);
			}else{
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_HEAL_FULL),name);
			}
			break;
			
		case 'D':
			if (defensiveEffect == null) {				
				defensiveEffect = new DefensiveEffect(item.getName(), 3,item.getValue());
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DEFENSE),name,item.getName(),item.getValue());
				inventory.remove(item);
				returning = new PlayerDamage(-1, 'n', null, null);
			}else{
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DEFENSE_FULL),name);			
			}			
			break;
			
		case 'O':
			returning = new PlayerDamage(item.getValue(),'o',name,item.getName());
			inventory.remove(item);
			break;
		case 'W':
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_USE),item.getName());
		case 'A':
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_USE),item.getName());
		default:
			break;
		}
		return returning;
	}
	
	/**
	 * Agrega un objeto al inventario
	 * @param item	Objetoa a agregar
	 * @return	Si se pudo agregar retorna true, de lo contrario retorna false.
	 */
	public boolean addItemToInventory(Item item)
	{
		if (inventory.size()<maxInventory) {
			inventory.add(item);
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_PICK),	name, item.getName());
			return true;			
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_FULL), item.getName());						
			return false;
		}
	}
	/**
	 * Elimina un objeto del inventario del usuario
	 * @param itemName Nombre del objeto a eliminar
	 */
	public void dropItem(String itemName)
	{
		Item item = findItem(itemName);
		inventory.remove(item);
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DROP),	itemName);
	}
	//TODO funcion EquipArmor(String itemName)
	//TODO funcion EquipWeapon(String itemName)
	//TODO funcion Calculo de exp nesesaria para siguiente nivel
	//TODO funcion addEXP(int EXP)
	//TODO funcion levelUp()
	//TODO Funcion canLevelUp()
	
	/**
	 * Remueve los effectos defensivos que ya no deben afectar al jugador
	 */
	public void turnPassed()
	{
		defensiveEffect.turnPassed();
		if (defensiveEffect.getRemainingTurns()<=0){
			defensiveEffect=null;
		}
	}
	
	public String getName() {
		return name;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public int getCurrentHP() {
		return currentHP;
	}
	public int getExp() {
		return exp;
	}
	public int getLevel() {
		return level;
	}
	public Item getWeapon() {
		return weapon;
	}
	public Item getArmor() {
		return armor;
	}
	public List<Item> getInventory() {
		return inventory;
	}
	public Point getInitPosition() {
		return initPosition;
	}
		
	
	

}
