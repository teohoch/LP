package ui;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import utils.Item;
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
		boolean validCommand = false;
		Scanner user_input = new Scanner( System.in );
		while (!validCommand) 
		{
			String command;
			command = user_input.nextLine();
			if (command.contains(" ")) {
				//TODO implentar manejo de "tomar <item>"
				//TODO implentar manejo de "pelear <enemigo>"
				//TODO implentar manejo de "equipar <item>"
				//TODO implentar manejo de "usar <item>"
				//TODO implentar manejo de "descartar <item>"
				System.out.println("Linea sin espacio");
			} else {
				switch (command) {
				case "terminar":
					keepPlaying = false;
					validCommand = true;
					lastCommand = command;
					exitMessage = MessageCode.MESSAGE_GAME_EXIT_USER;
					break;
				case "inventario":
					showInventory();
					validCommand = true;
					break;
				case "observar":
					observe();
					validCommand = true;
					break;	
				default:
					System.out.println("default");
					break;
				}
			}
			
		}
	}
	private void EnemyTurn()
	{
		
	}

	public void Start()
	{
		keepPlaying = true;
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INIT));
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
		System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_EXIT), messenger.getMessage(exitMessage));
		
	}
	private void showInventory(){
		List<Item> inventario = maze.getPlayerInventory();
		if (inventario.size()!=0) {
			String formatListItem = "";
			boolean first = true;
			for (Item item : inventario) {
				MessageCode itemType = null;
				switch (item.getType()) {
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
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_CONTENT),maze.getPlayerName(),formatListItem);						
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_INVENTORY_EMPTY), maze.getPlayerName());
		}
	}
	private void observe(){
		showCurrentEnemies();
		showCurrentItems();
		showPosibleMovement();
		
	}
	private void showPosibleMovement() {
		// TODO Auto-generated method stub
		
	}
	private void showCurrentItems() {
		// TODO Auto-generated method stub
		
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
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ENEMY_CONTENT), maze.getPlayerName(),formatListEnemy);
		} else {
			System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_CELL_ENEMY_EMPTY));
		}
		
	}
}
