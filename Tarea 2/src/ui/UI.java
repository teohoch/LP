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

public class UI {
	
	private MessageReader messenger;
	private Dungeon maze;
	
	boolean keepPlaying;
	boolean running;
	
	private int currentTurn;
	private int winCountUp;
	
	private MessageCode exitMessage;
	private String lastCommand;
	
	
	public UI(String enemiesPath, String itemsPath, String boardPath, String playerPath, String messengerPath)
	{
		try {
			messenger = new MessageReader(messengerPath);
		} catch (IOException e) {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		}
		maze = new Dungeon(enemiesPath, itemsPath, boardPath, playerPath, messenger);
		currentTurn = 0;
		winCountUp = 0;
		running = false;
	}
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
	
	private void PlayerTurn()
	{
		System.out.printf("Inicio turno: %d \n",currentTurn);
		boolean validCommand = false;
		Scanner user_input = new Scanner( System.in );
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
		maze.playerTurnPassed();
		currentTurn++;
	}
	
	
	private void EnemyTurn()
	{
		
	}

	public void Start()
	{
		keepPlaying = true;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INIT)+"\n");
		while(keepPlaying)
		{
			checkWinConditions();
			if (keepPlaying) {
				PlayerTurn();
				if (keepPlaying) {
					EnemyTurn();
				}
			}			
		}
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_EXIT)+"\n", messenger.getMessage(exitMessage));
		
	}
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
	private void observe(){
		showCurrentEnemies();
		showCurrentItems();
		showPosibleMovement();
		
	}
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
	private boolean setPlayerWalk() {
		return maze.playerWalk();
	}
	
	private String generateMovementLastCommand(){
		return ((running) ? "correr" : "caminar");
	}
	private void atackAllEnemies(PlayerDamage status) {
		List<Enemy> currentEnemies = maze.getCurrentLocationEnemies();
		if (currentEnemies.size() > 0) {
			maze.attackAllCurrentEnemies(status);			
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_NONE)+"\n", status.getOrigin(),status.getWeaponOrigin());
		}
		
	}
}
