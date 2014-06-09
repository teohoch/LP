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
	
	private int currentExp;
	private int nextLevelExp;
	private int level;
	
	private int attack;
	private int defense;
	
	private Item weapon;
	private Item armor;
	
	private List <Item> inventory;
	private int maxInventory;
	private Point initPosition;
	
	private DefensiveEffect defensiveEffect;
	private int running;
	
	private MessageReader messenger;
	
	public Player(String filePath,MessageReader messenger)
	{
		this.messenger = messenger;
		loadPlayer(filePath);
		defensiveEffect = null;
		currentExp = 0;
		level = 1;
		maxInventory = 10;
		running = 0;
		
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
		inventory = tempList;
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
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_ENEMY)+"\n",damage.getOrigin(),name,efectiveDamage);
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
			}else{
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_INVENTORY)+"\n",name);
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
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_HEAL)+"\n",name,item.getName(),recoverHP);
				inventory.remove(item);
				returning = new PlayerDamage(-1, 'n', null, null);
			}else{
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_HEAL_FULL)+"\n",name);
			}
			break;
			
		case 'D':
			if (defensiveEffect == null) {				
				defensiveEffect = new DefensiveEffect(item.getName(), 3,item.getValue());
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DEFENSE)+"\n",name,item.getName(),item.getValue());
				inventory.remove(item);
				returning = new PlayerDamage(-1, 'n', null, null);
			}else{
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DEFENSE_FULL)+"\n",name);			
			}			
			break;
			
		case 'O':
			returning = new PlayerDamage(item.getValue(),'o',name,item.getName());
			inventory.remove(item);
			break;
		case 'W':
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_USE)+"\n",item.getName());
		case 'A':
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_USE)+"\n",item.getName());
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
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_PICK)+"\n",	name, item.getName());
			return true;			
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_FULL)+"\n", item.getName());						
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
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DROP)+"\n",	itemName);
	}
	/**
	 * Equipa la armadura seleccionada
	 * @param armor item armadura a equipar
	 */
	private void EquipArmor(Item armor)
	{		
			Item newArmor = armor;
			Item oldArmor = this.armor;
			this.armor = newArmor;	
			inventory.remove(newArmor);
			inventory.add(oldArmor);
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_EQUIP_ARMOR)+"\n", name,this.armor.getName());
	}
	/**
	 * Equipa la armadura seleccionada
	 * @param weapon item armadura a equipar
	 */
	private void EquipWeapon(Item weapon)
	{		
			Item newWeapon = weapon;
			Item oldWeapon = this.weapon;
			this.armor = newWeapon;	
			inventory.remove(newWeapon);
			inventory.add(oldWeapon);
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_EQUIP_ARMOR)+"\n", name,this.weapon.getName());
	}
	/**
	 * Equipa la armadura o arma segun corresponda. Chequea que el objeto este en el inventario, y despues que sea del tipo correcto.
	 * @param itemName Nombre del arma/armadura a equipar
	 * @return Si la operacion termina con exito retorna true, si el objeto no esta en el inventario o no es del tipo correcto, retorna false.
	 */
	public boolean Equip(String itemName)
	{
		boolean returning = false;
		if (InInventory(itemName)) {
			Item item = findItem(itemName);
			switch (item.getType()) {
			case 'A':
				EquipArmor(item);
				returning = true;
				break;
			case 'W':
				EquipWeapon(item);
				returning = true;
				break;
			default:
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_EQUIP)+"\n", item.getName());
				break;
			}
		}		
		return returning;
	}
	/**
	 * Calcula la Experiencia nesesaria para alcansar el siguiente nivel;
	 */
	private void NextLevelExp()
	{
		int nextLevel = level +1;
		double sum = 0;
		for (int i = 1; i < (nextLevel-1); i++) {
			sum = sum + Math.pow(2, (i/7));			
		}
		sum = 9*sum;
		this.nextLevelExp = (int) Math.round(sum);
	}
	/**
	 * Agrega los puntos de experiencia a los puntos acumulados.
	 * @param newExp Puntos de experiencia a añadir
	 */
	public void AddExp(int newExp)
	{
		currentExp = currentExp + newExp;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_XP_EARN)+"\n",name,newExp);
	}
	/**
	 * Retorna si el jugador puede subir de nivel
	 * @return 
	 */
	public boolean CanLevelUp()
	{
		return (currentExp>=nextLevelExp);
	}
	
	/**
	 * Sube de nivel al jugador. Se le agregan 2 slots a la capacidad maxima del inventario,
	 *  se le aumenta 10 a los HP Maximos y a los HP actuales.
	 */
	public void levelUp()
	{
		maxInventory = maxInventory + 2;
		maxHP = maxHP + 10;
		currentHP = (currentHP+10 <= maxHP) ? currentHP+10 : maxHP;
		NextLevelExp();
		level++;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_LEVEL_UP)+"\n",name,level,maxHP,maxInventory);
	}
	/**
	 * 
	 * @return Retorna si el jugador esta corriendo o no
	 */
	public boolean isRunning()
	{
		return (running > 2);
	}
	/**
	 * 
	 * @return Retorna si el jugador puede volver a correr
	 */
	private boolean canRun()
	{
		return (running==0);
	}
	/**
	 * Comprueba si el jugador esta corriendo y si puede volver a correr. 
	 * De no estar corriendo y puede volver a correr, el jugador comienza a correr.
	 * @return Retorna true solamente si el jugador paso de estar caminando a corriendo.
	 */
	public boolean run()
	{
		if (isRunning()) {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_RUN)+"\n", name);
			return false;
		} else {
			if (canRun()) {
				running = 7;
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_TYPE_RUN)+"\n", name);
				return true;
			} else {
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_RUN_COOLDOWN)+"\n", name);
				return false;
			}
		}
	}
	/**
	 * Comprueba si el jugador esta corriendo. Si es asi, lo hace caminar y descansar 1 turno (running = 1)
	 * @return Retorna true solo si el jugado paso de correr a caminar.
	 */
	public boolean walk()
	{
		if (isRunning()) {
			running = 1;
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_TYPE_WALK)+"\n", name);
			return true;			
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_WALK)+"\n", name);
			return false;
		}
	}
	/**
	 * Hace que el jugador descanse disminuyendo su contador de Running
	 */
	private void rest()
	{
		if (running>0 && running <=2) {
			running--;
			if (running ==0) {
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_COOLDOWN_END)+"\n", name);		
			}
		}
		
	}
	/**
	 * reduce el contador running para indicar que el jugador corrio.
	 */
	private void runningTick()
	{
		if (isRunning()) 
		{
			running--;
		}
		if (running ==2) {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_RUN_END)+"\n", name);		
		}
	}
	
	/**
	 * Remueve los effectos defensivos que ya no deben afectar al jugador y 
	 * hace que el jugador descanse
	 */
	public void turnPassed()
	{
		rest();
		runningTick();
		if(defensiveEffect!=null){
			defensiveEffect.turnPassed();
			if (defensiveEffect.getRemainingTurns()<=0){
				defensiveEffect=null;
			}
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
		return currentExp;
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
	public boolean IsHeDead() {
		return (currentHP<1);
	}
		
	
	

}
