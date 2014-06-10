package ui;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import utils.Item;
import utils.PlayerDamage;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader.MessageCode;
import dungeon.Dungeon;
import dungeon.Enemy;
/**
 * Interfaz del Juego con el Usuario. Implenta el manejo de las entradas, ademas de activar los turnos.
 * @author teohoch
 *
 */
public class UI {
	
	private MessageReader messenger;
	private Dungeon maze;
	
	boolean keepPlaying;
	boolean running;
	
	private int winCountUp;
	
	private MessageCode exitMessage;
	private String lastCommand;
	
	/**
	 * Constructor de clase
	 * @param enemiesPath Ruta del archivo que contiene a los enemigos
	 * @param itemsPath Ruta del archivo que contiene a los items del mapa
	 * @param boardPath Ruta del archivo que contiene los datos del mapa
	 * @param playerPath Ruta del archivo que contiene los datos del jugador
	 * @param messengerPath Ruta del archivo que contiene los mensages.
	 */
	public UI(String enemiesPath, String itemsPath, String boardPath, String playerPath, String messengerPath)
	{
		try {
			messenger = new MessageReader(messengerPath);
		} catch (IOException e) {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		}
		maze = new Dungeon(enemiesPath, itemsPath, boardPath, playerPath, messenger);
		winCountUp = 0;
		running = false;
	}
	/**
	 * Implementa el chequeo de condiciones de victoria y derrota.
	 */
	private void checkWinConditions()
	{
		if (maze.getEnemyTotalNumber()==0) {
			winCountUp++;
			if (winCountUp>9) {
				exitMessage =MessageCode.MESSAGE_GAME_EXIT_VICTORY_ENEMIES;
				keepPlaying = false;
			}
		}
		if (maze.getNumberOfNotVisitedTiles()==0){
			exitMessage =MessageCode.MESSAGE_GAME_EXIT_VICTORY_CELLS;
			keepPlaying = false;			
		}
		if (maze.isPlayerDead()) {
			exitMessage = MessageCode.MESSAGE_GAME_EXIT_DEFEAT;
			keepPlaying = false;
		}
	}
	/**
	 * Implementa el manejo el Input del usuario, manejando asi el turno de este.
	 * @param user_input Objeto scanner para recibir el input del Usuario
	 */
	private void PlayerTurn(Scanner user_input)
	{
		boolean validCommand = false;
		while (!validCommand) 
		{
			String command;
			String errorCode = null;
			command = user_input.nextLine();
			
			String split[]= command.split("\\s+");
			String action = split[0];
			String argument = "";
			if (split.length>1) {
				argument = command.substring(command.indexOf(' ')+1);
			}
			
				switch (action) {
				case "terminar":
					keepPlaying = false;
					validCommand = true;
					lastCommand = action;
					exitMessage = MessageCode.MESSAGE_GAME_EXIT_USER;
					break;
				case "inventario":
					showInventory();
					validCommand = true;
					lastCommand = action;
					break;
				case "observar":
					observe();
					validCommand = true;
					lastCommand = action;
					break;
				case "caminar":
					if (setPlayerWalk()){
						lastCommand = action;	
						validCommand = true;
						running = false;
					}else{
						errorCode= String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_WALK), maze.getPlayerName());
					}
					break;
				case "correr":
					int state = maze.playerRun();
					switch (state) {
						case 1:
							lastCommand = action;	
							validCommand = true;
							running = true;
							break;
						case -1:
							errorCode= String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_RUN), maze.getPlayerName());
							break;
						case -2:
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_RUN_COOLDOWN), maze.getPlayerName());
							break;
						}												
					break;
				case "avanzar":
					validCommand = maze.moveCurrentPosition(1);
					lastCommand = generateMovementLastCommand();
					if (!validCommand) {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_MOVE);
					}
					
					break;
				case "derecha":
					validCommand = maze.moveCurrentPosition(2);
					lastCommand = generateMovementLastCommand();
					if (!validCommand) {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_MOVE);
					}
					break;
				case "retroceder":
					validCommand = maze.moveCurrentPosition(3);
					lastCommand = generateMovementLastCommand();
					if (!validCommand) {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_MOVE);
					}
					break;
				case "izquierda":
					validCommand = maze.moveCurrentPosition(4);
					lastCommand = generateMovementLastCommand();
					if (!validCommand) {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_MOVE);
					}
					break;
				case "tomar":
					if (split.length>1) {
						if (maze.itemIsInCurrent(argument)) {
							if (maze.playerPickItem(argument)) {
								lastCommand = action;
								validCommand = true;
							} else {
								errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_FULL), argument);
							}
						} else {
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_CELL_ITEM),argument);
						}
					} else {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ARGUMENT);
					}
					break;
				case "pelear":
					if (split.length>1) {
						if (maze.enemyInCurrent(argument)) {
							maze.playerAtackEnemy(argument);
							lastCommand = action;
							validCommand = true;
						} else {
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ATTACK),argument);
						}
					} else {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ARGUMENT);
					}
					break;
				case "equipar":
					if (split.length>1) {
						if (maze.isInPlayerInventory(argument)) {
							if(maze.playerEquip(argument)){
								lastCommand = action;
								validCommand = true;
							}else{
								errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_EQUIP),argument);
							}
						} else {
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_INVENTORY), argument);
						}
					} else {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ARGUMENT);
					}
					break;
				case "usar":
					if (split.length>1) {
						if (maze.isInPlayerInventory(argument)) {
							PlayerDamage status = maze.usePlayerItem(argument);
							if (status!=null) {
								if (status.getType()=='e') {
									switch (status.getValue()) {
									case -1:
										errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_TYPE_USE), argument);
										break;
									case -2:
										errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_HEAL_FULL), maze.getPlayerName());
										break;
									case -3:
										errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ITEM_DEFENSE_FULL), maze.getPlayerName());
										break;
									}											
								} else {
									//Se utilizo un objeto ofensivo
									lastCommand = action;
									validCommand = true;
									atackAllEnemies(status);
								}								
							} else {
								lastCommand = action;
								validCommand = true;
							}							
						} else {
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_INVENTORY), argument);
						}
					} else {
						errorCode =messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ARGUMENT);
						}
					break;
				case "descartar":
					if (split.length>1) {
						if (maze.isInPlayerInventory(argument)) {
							maze.playerDropItem(argument);
							lastCommand = action;
							validCommand = true;
						}else{
							errorCode = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_INVENTORY), argument);
						}
					} else {
						errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_ARGUMENT);
					}
					break;
				default:
					errorCode = messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR_COMMAND);
					break;
				}
				if (!validCommand) {
					System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ERROR)+"\n", command,errorCode);				
				}	
		}
	}
	
	/**
	 * Implementa el inicio del juego, manteniendose en un loop mientras este se ejecuta.
	 * Continua a menos que el usuario gane, pierda o cierre el juego.
	 */
	public void Start()
	{
		keepPlaying = true;
		Scanner user_input = new Scanner(System.in);
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INIT)+"\n");
		while(keepPlaying)
		{
			running = (!maze.playerTurnPassed()&&running);
			checkWinConditions();
			if (keepPlaying) {
				PlayerTurn(user_input);
				if (keepPlaying) {
					maze.EnemyTurn(lastCommand);
				}
			}			
		}
		user_input.close();
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_EXIT)+"\n", messenger.getMessage(exitMessage));
		
	}
	/**
	 * Implenta el mostrar los contenidos del inventario con los MessageCodes.
	 */
	private void showInventory(){
		List<Item> inventario = maze.getPlayerInventory();
		if (inventario.size()!=0) {
			String formatListItem = "";
			boolean first = true;
			for (Item item : inventario) {
				MessageCode itemType = itemTypeMessage(item.getType());
				String temp = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_ITEM),
						item.getName(),messenger.getMessage(itemType),item.getValue());
				if (!first) {
					formatListItem = String.format(
							messenger.getMessage(MessageCode.MESSAGE_GAME_CONCATENATOR), formatListItem,temp);
				}else{
					formatListItem = temp;
				}
				first = false;
			}
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_CONTENT)+"\n",maze.getPlayerName(),formatListItem);						
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_EMPTY)+"\n", maze.getPlayerName());
		}
	}
	/**
	 * Implenta el observar la casilla
	 */
	private void observe(){
		showCurrentEnemies();
		showCurrentItems();
		showPosibleMovement();
		
	}
	/**
	 * Muestra las opciones de movimiento que tiene el usuario, utilisando los MessageCodes.
	 */
	private void showPosibleMovement() {
		List<Integer> posibleMovements = maze.getPosibleMovements();
		if (posibleMovements.size()!=0) {
			String formatMovementList = "";
			for (Integer integer : posibleMovements) {
				
				MessageCode directionMessage = maze.directionMessage(integer);
				
				boolean isLast = (integer==posibleMovements.get(posibleMovements.size()-1));
				boolean isFirst = (integer==posibleMovements.get(0));
				
				
				if (isFirst) {
					formatMovementList = messenger.getMessage(directionMessage);
				}else{
					if (isLast) {
						formatMovementList = String.format(
								messenger.getMessage(MessageCode.MESSAGE_GAME_OR_CONCATENATOR), 
								formatMovementList,messenger.getMessage(directionMessage));										
					} else {
						formatMovementList = String.format(
								messenger.getMessage(MessageCode.MESSAGE_GAME_CONCATENATOR), 
								formatMovementList,messenger.getMessage(directionMessage));
					}
				}
			}
			System.out.printf(messenger.getMessage(
					MessageCode.MESSAGE_GAME_CELL_MOVEMENT_OPTIONS)+"\n", 
					maze.getPlayerName(),formatMovementList);
		} else {
			System.out.printf(messenger.getMessage(
					MessageCode.MESSAGE_GAME_CELL_MOVEMENT_EMPTY)+"\n", 
					maze.getPlayerName());
		}
		
	}
	/**
	 * Muestra los objetos en la presente casilla, utilisando los MessageCodes.
	 */
	private void showCurrentItems() {
		List<Item> currentItems = maze.getCurrentLocationItems();
		if (currentItems.size()!=0) {
			String formatListItem = "";
			boolean first = true;
			for (Item item : currentItems) {
				MessageCode itemType = itemTypeMessage(item.getType());
				
				String temp = String.format(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_ITEM),
						item.getName(),messenger.getMessage(itemType),item.getValue());
				
				if (!first) {
					formatListItem = String.format(
							messenger.getMessage(MessageCode.MESSAGE_GAME_CONCATENATOR), formatListItem,temp);
				}else{
					formatListItem = temp;
				}
				first = false;
			}
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ITEM_CONTENT)+"\n",maze.getPlayerName(),formatListItem);						
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ITEM_EMPTY)+"\n");
		}
		
	}
	/**
	 * Muestra los enemigos en la presente casilla, utilisando los MessageCodes.
	 */
	private void showCurrentEnemies() {
		List<Enemy> currentEnemies = maze.getCurrentLocationEnemies();
		if (currentEnemies.size()!=0) {
			boolean first = true;
			String formatListEnemy="";
			for (Enemy enemy : currentEnemies) {
				String temp = enemy.getName();
				if (!first) {
					formatListEnemy = String.format(
							messenger.getMessage(MessageCode.MESSAGE_GAME_CONCATENATOR), formatListEnemy,temp);
				}else{
					formatListEnemy = temp;
				}
				first = false;
			}
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ENEMY_CONTENT)+"\n", maze.getPlayerName(),formatListEnemy);
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ENEMY_EMPTY)+"\n");
		}
		
	}
	/**
	 * Entrega el MessageCode asociado al tipo de objeto
	 * @param type tipo de objeto
	 * @return MessageCode asociado
	 */
	private MessageCode itemTypeMessage(char type){
		MessageCode itemType = null;
		switch (type) {
		case 'W':
			itemType = MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_WEAPON;
			break;
		case 'A':
			itemType = MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_ARMOR;
			break;
		case 'H':
			itemType = MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_HEALING;
			break;
		case 'D':
			itemType = MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_DEFENSE;
			break;
		case 'O':
			itemType = MessageCode.MESSAGE_GAME_INVENTORY_CONTENT_TYPE_OFFENSE;
			break;
		}
		return itemType;
	}
	/**
	 * Clase wrapper de Dungeon.playerWalk()
	 * Hace que el jugador camine, si es que no lo esta haciendo ya.
	 * @return retorna true si el jugador paso de correr a caminar
	 */
	private boolean setPlayerWalk() {
		return maze.playerWalk();
	}
	
	/**
	 * Cuando el jugador se mueve (arriba, etc) 
	 * es utilizado para generar el un lastCommand que indique si el usuario estaba corriendo o caminando. 
	 * De esta forma si el usuario se movio corriendo, retorna "correrMovimient", mientras que si fue caminando retorna "caminando"
	 * @return Si el usuario se movio corriendo, retorna "correrMovimient", mientras que si fue caminando retorna "caminando"
	 */
	private String generateMovementLastCommand(){
		return ((running) ? "correrMovimiento" : "caminar");
	}
	/**
	 * Comprueba si el en la casilla hay algun enemigo, avisando si no lo hay.
	 * Si lo hay, ataca a todos los enemigos con el Player damage recivido
	 * @param status el Player damage a inflinjir en los enemigos.
	 */
	private void atackAllEnemies(PlayerDamage status) {
		List<Enemy> currentEnemies = maze.getCurrentLocationEnemies();
		if (currentEnemies.size() > 0) {
			maze.attackAllCurrentEnemies(status);			
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_NONE)+"\n", status.getOrigin(),status.getWeaponOrigin());
		}
		
	}
}
