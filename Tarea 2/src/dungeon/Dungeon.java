package dungeon;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.utfsm.inf.lp.sem12014.mud.input.BoardReader;
import cl.utfsm.inf.lp.sem12014.mud.input.EnemyReader;
import cl.utfsm.inf.lp.sem12014.mud.input.ItemReader;
import utils.Item;

public class Dungeon {
	private Point currentPosition;
	
	private String enemiesPath;
	private String itemsPath;
	private String boardPath;
	
	private List <Enemy> dungeonEnemies;
	private List <Item> dungeonItems;
	
	private List <Enemy> currentLocationEnemies;
	private List <Item> currentLocationItems;
	
	private Dimension mapSize;
	private List <Point> map;
	
	public Dungeon(String enemiesPath, String itemsPath, String boardPath, Point startPosition)
	{
		this.enemiesPath = enemiesPath;
		this.itemsPath = itemsPath;
		this.boardPath = boardPath;
		this.currentPosition = startPosition;//TODO Set StartPosition from Player.mud File
		
		LoadEnemies();
		LoadItems();
		LoadMap();
		//TODO Implement LoadPlayer()
	}
	/**
	 * Carga desde archivo los enemigos del tablero
	 */
	private void LoadEnemies()
	{
		EnemyReader<Enemy> eReader = null;
		try {
			eReader = new EnemyReader<Enemy>(enemiesPath, Enemy.class);
		} catch (IOException e) {
			e.printStackTrace();			
		}
		List<Enemy> enemiesList = null;
		try {
			enemiesList = eReader.getEnemies();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		 dungeonEnemies = enemiesList;		
	}
	/**
	 * Carga desde archivo los Items del tablero
	 */
	private void LoadItems()
	{
		ItemReader iReader = null;
		try {
			iReader = new ItemReader(itemsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Item> itemsList = new ArrayList<Item>();
		String items[] = iReader.getKeys();
		for (String item : items) {
			char type = iReader.getType(item);
			Point location = iReader.getPosition(item);
			int value = iReader.getValue(item);
			Item Temp = new Item(item, type, value, location);
			itemsList.add(Temp);			
		}
		dungeonItems = itemsList; 
	}
	
	/**
	 * Carga desde archivo el mapa del tablero
	 */
	private void LoadMap()
	{
		BoardReader bReader = null;
		
		try {
			bReader = new BoardReader(boardPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapSize = bReader.getDimension();
		map = bReader.getImpassablePoints();
		
	}
	/**
	 * Selecciona los items que estan en la casilla actual
	 */
	private void RetrieveCurrentPositionItems()
	{
		List <Item> currentPositionItems = new ArrayList<Item>();
		for (Item item : dungeonItems) 
		{
			if (item.getLocation()== currentPosition) 
			{
				currentPositionItems.add(item);
			}			
		}
		this.currentLocationItems = currentPositionItems;
	}
	/**
	 * Selecciona los Enemigos que estan en la casilla actual
	 */
	private void RetrieveCurrentPositionEnemies()
	{
		List <Enemy> currentPositionEnemies = new ArrayList<Enemy>();
		for (Enemy enemy : dungeonEnemies) 
		{
			if (enemy.getLocation()== currentPosition) 
			{
				currentPositionEnemies.add(enemy);
			}			
		}
		this.currentLocationEnemies = currentPositionEnemies;
	}
	/**
	 * Checkea que el punto de destino esta dentro del tablero y es pasable.
	 * @param destination Destino a comprobar
	 * @return boleano indicando si el destino es o no valido.
	 */
	private boolean CheckPassablePoint(Point destination)
	{
		if (destination.x < mapSize.width && destination.x >= 0 && destination.y < mapSize.height && destination.y >= 0) {
			return !map.contains(destination);
		}else {
			return false;
		}
				
	}
	/**
	 * Funcion que genera un movimiento tentativo, 
	 * devolviendo la posicion el la cual se encontraria el jugador despues de este.
	 * @param direction Direccion del moviemiento Up = 1; Right	= 2; Down = 3; Left	= 4;
	 * @param value Valor de movimiento, caminar => 1, correr => 2
	 * @return Devulve el punto de destino despues del movimiento
	 */
	private Point GenerateMovement(int direction, int value)
	{
		Point destination = new Point(currentPosition.x, currentPosition.y);
		switch (direction) {
		case 1:
			destination.y = destination.y + value;
			break;
		case 2:
			destination.x = destination.x - value;
			break;
		case 3:
			destination.y = destination.y - value;
			break;
		case 4:
			destination.x = destination.x + value;
		}
		return destination;
	}
	/**
	 * Checkea que el movimiento. 
	 * Si se esta caminando, checkea solamente un movimiento. si esta corriendo, checkea dos. 
	 * @param direction Direccion del movimiento
	 * @param running	Si el movimiento es corriendo o no.
	 * @return Punto en el cual se encuentra el jugador despues del movimiento.
	 */
	private Point CheckMovement(int direction, boolean running)
	{
		
		Point destination = GenerateMovement(direction, 1);
		if (CheckPassablePoint(destination)) 
		{
			if (running) 
			{
				Point destinationRunning = GenerateMovement(direction, 2);
				if (CheckPassablePoint(destinationRunning)) 
				{
					return destinationRunning;					
				} else 
				{
					return destination;
				}
			} else 
			{
				return destination;
			}
			
		} else 
		{
			return currentPosition;
		}	

	}
	
	/**
	 * Chekea si el movimiento es valido, y si lo es, chekea que tanto se movio
	 * @param direction Direccion del moviento
	 * @param running	Si esta corriendo o no
	 * @return Devuelve el resultado de la operacion. Posibles resultados:
	 * -1 -> No se puedo mover, movimiento invalido
	 *  1 -> Se movio una casilla.
	 *  2 -> Se movio dos casillas.
	 */
	public int MoveCurrentPosition(int direction, boolean running)
	{
		Point destination = CheckMovement(direction, running);
		if (destination != currentPosition) {
			
			RetrieveCurrentPositionEnemies();
			RetrieveCurrentPositionItems();
			
			if (destination.distance(currentPosition)>1) {
				return 2; //se movio 2 espacios
			} else {
				return 1; // se movio 1 espacio
			}
			
			
		} else {
			return -1;
		}
		
		
	}

	public List<Enemy> getCurrentLocationEnemies() {
		return currentLocationEnemies;
	}

	public List<Item> getCurrentLocationItems() {
		return currentLocationItems;
	}
	

}
