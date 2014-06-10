package dungeon;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.utfsm.inf.lp.sem12014.mud.input.BoardReader;
import cl.utfsm.inf.lp.sem12014.mud.input.EnemyReader;
import cl.utfsm.inf.lp.sem12014.mud.input.ItemReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader.MessageCode;
import utils.Damage;
import utils.Item;
import utils.PlayerDamage;
/**
 * Clase que contiene a los enemigos, jugador y elementos dentro del juego.
 *  Maneja las interacciones entre estos, tales como ataques entre ellos, o movimientos del jugador, entre otros
 * @author teohoch
 *
 */
public class Dungeon {
	private MessageReader messenger;
	private Point currentPosition;
	
	private String enemiesPath;
	private String itemsPath;
	private String boardPath;
	private String playerPath;
	
	private List <Enemy> dungeonEnemies;
	private List <Item> dungeonItems;
	
	private List <Enemy> currentLocationEnemies;
	private List <Item> currentLocationItems;
	
	private Dimension mapSize;
	private List <Point> map;
	private List <Point> notVisited;
	
	private Player player;
	/**
	 * Constructor de la clase
	 * @param enemiesPath Ruta del archivo que contiene a los enemigos
	 * @param itemsPath Ruta del archivo que contiene a los items del mapa
	 * @param boardPath Ruta del archivo que contiene los datos del mapa
	 * @param playerPath Ruta del archivo que contiene los datos del jugador
	 * @param messenger Objeto MessageReader utilizado para generar los mensages para el usuario.
	 */
	public Dungeon(String enemiesPath, String itemsPath, String boardPath, String playerPath, MessageReader messenger)
	{
		this.enemiesPath = enemiesPath;
		this.itemsPath = itemsPath;
		this.boardPath = boardPath;
		this.playerPath = playerPath;
		this.messenger = messenger;
		
		this.player = new Player(this.playerPath,messenger);
		this.currentPosition = player.getInitPosition();
		
		
		LoadEnemies();
		currentLocationEnemies = new ArrayList<Enemy> ();
		retrieveCurrentPositionEnemies();
		
		LoadItems();
		currentLocationItems = new ArrayList<Item> ();
		retrieveCurrentPositionItems();
		LoadMap();
		
		
	}
	/**
	 * Carga desde archivo los enemigos del tablero y los inicializa
	 */
	private void LoadEnemies()
	{
		EnemyReader<Enemy> eReader = null;
		try {
			eReader = new EnemyReader<Enemy>(enemiesPath, Enemy.class);
		} catch (IOException e) {
			System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();			
		}
		List<Enemy> enemiesList = null;
		try {
			enemiesList = eReader.getEnemies();
		} catch (InstantiationException e) {
			System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		}
		for (Enemy enemy : enemiesList) {
			enemy.init(messenger);
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
			System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
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
			System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_GENERIC_ERROR));
			e.printStackTrace();
		}
		mapSize = bReader.getDimension();
		map = bReader.getImpassablePoints();
		
		notVisited = new ArrayList<Point>();
		
		for (int x = 0; x < mapSize.width; x++) {
			for (int y = 0; y < mapSize.height; y++) {
				notVisited.add(new Point(x,y));
			}			
		}
		notVisited.remove(currentPosition);
		for (Point point : map) {
			if (notVisited.contains(point)) {
				notVisited.remove(point);				
			}			
		}		
	}
	/**
	 * Selecciona los items que estan en la casilla actual, los carga y devuelve los de la casilla anterior
	 */
	private void retrieveCurrentPositionItems()
	{
		List <Item> currentPositionItems = new ArrayList<Item>();
		List <Item> oldPositionItems = this.currentLocationItems;
		for (Item item : dungeonItems) 
		{
			if (item.getLocation().x== currentPosition.x && item.getLocation().y== currentPosition.y) 
			{
				currentPositionItems.add(item);
			}			
		}
		for (Item item : currentPositionItems) {
			dungeonItems.remove(item);
		}
		for (Item item : oldPositionItems) {
			dungeonItems.add(item);
		}
		this.currentLocationItems = currentPositionItems;
	}
	/**
	 * Selecciona los Enemigos que estan en la casilla actual, los carga y devuelve los de la casilla anterior
	 */
	private void retrieveCurrentPositionEnemies()
	{
		List <Enemy> currentPositionEnemies = new ArrayList<Enemy>();
		List <Enemy> oldPositionEnemies = this.currentLocationEnemies;
		
		for (Enemy enemy : dungeonEnemies) //Recolecto todos los enemigos 
		{
			if (enemy.getLocation().x== currentPosition.x && enemy.getLocation().y== currentPosition.y  ) 
			{
				currentPositionEnemies.add(enemy);
			}			
		}
		for (Enemy enemy : currentPositionEnemies)//los elimino de la lista general
		{
			dungeonEnemies.remove(enemy);
		}
		for (Enemy enemy : oldPositionEnemies)//y devuelvo a los enemigos de la posicion anterior a la lista general
		{
			enemy.setAttacked(false);
			dungeonEnemies.add(enemy);
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
	private Point generateMovement(int direction, int value)
	{
		Point destination = new Point(currentPosition.x, currentPosition.y);
		switch (direction) {
		case 1:
			destination.y = destination.y + value;
			break;
		case 2:
			destination.x = destination.x + value;
			break;
		case 3:
			destination.y = destination.y - value;
			break;
		case 4:
			destination.x = destination.x - value;
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
	private Point checkMovement(int direction, boolean running)
	{
		
		Point destination = generateMovement(direction, 1);
		if (CheckPassablePoint(destination)) 
		{
			if (running) 
			{
				Point destinationRunning = generateMovement(direction, 2);
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
	 * @return Devuelve el resultado de la operacion. Posibles resultados:
	 * -1 -> No se puedo mover, movimiento invalido
	 *  1 -> Se movio una casilla.
	 *  2 -> Se movio dos casillas.
	 */
	public boolean moveCurrentPosition(int direction)
	{
		Point oldPosition = currentPosition;
		Point destination = checkMovement(direction, player.isRunning());
		if (destination != currentPosition) {
			
			currentPosition = destination;
			retrieveCurrentPositionEnemies();
			retrieveCurrentPositionItems();
			notVisited.remove(destination);
			
			if ((destination.distance(oldPosition)>1)) {
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE)+"\n",
						player.getName(),
						messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_TWO),
						messenger.getMessage(directionMessage(direction)));
				return true;
			} else {
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE)+"\n",
						player.getName(),
						messenger.getMessage(MessageCode.MESSAGE_GAME_MOVE_ONE),
						messenger.getMessage(directionMessage(direction)));
				return true;
			}

			
		} else {
			return false;
		}		
	}
	/**
	 * Comprueba los posibles movientos para el jugador
	 * @return Retorna una lista con Integer que representan las direcciones en las cuales se puede mover el jugador
	 * Up = 1; Right	= 2; Down = 3; Left	= 4;
	 */
	public List<Integer> getPosibleMovements()
	{
		List <Integer> posibleMovements = new ArrayList<Integer>();
		for (int i = 1; i <= 4; i++) {
			if (checkMovement(i,false)	!= currentPosition) {
				posibleMovements.add(i);
			}			
		}
		return posibleMovements;
	}	
	/**
	 * A partir de una direccion, entrega el MessageCode asociado.
	 * @param direction entero en la siguiente forma:
	 * <p> Avanzar = 1
	 * <p> Derecha = 2
	 * <p> Abajo = 3
	 * <p> Izquierda = 4;
	 * @return Retorna el MessageCode asociado a la direccion.
	 */
	public MessageCode directionMessage(int direction){
		MessageCode directionMessage = null;
		switch (direction) {
		case 1:
			directionMessage = MessageCode.MESSAGE_GAME_MOVE_UP;
			break;
		case 2:
			directionMessage = MessageCode.MESSAGE_GAME_MOVE_RIGHT;
			break;
		case 3:
			directionMessage = MessageCode.MESSAGE_GAME_MOVE_DOWN;
			break;
		case 4:
			directionMessage = MessageCode.MESSAGE_GAME_MOVE_LEFT;
			break;
		}
		return directionMessage;
	}
	/**
	 * Clase wrapper de Player.turnPassed()
	 * Efectua los cambios nesesarios para cuando pasa un turno
	 * @return Retorna true cuando el jugador paso de estar corriendo a estar caminando.
	 */
	public boolean playerTurnPassed(){
		return player.turnPassed();
	}
	/**
	 * Clase wrapper de Player.walk()
	 * Hace que el jugador camine, si no lo esta haciendo ya.
	 * @return Retorna true solo si el jugado paso de correr a caminar.
	 */
	public boolean playerWalk(){
		return player.walk();
	}
	/**
	 * Clase wrapper de Player.run()
	 * Hace que el jugador corra, si no lo esta haciendo ya o esta muy cansado.
	 * @return Retorna Retorna -1 si el jugador ya estaba corriendo, -2 si todavia debe descansar y 1 si paso de caminar a correr.
	 */
	public int playerRun() {
		return player.run();
	}
	
	public List<Enemy> getCurrentLocationEnemies() {
		return currentLocationEnemies;
	}

	public List<Item> getCurrentLocationItems() {
		return currentLocationItems;
	}
	/**
	 * Entrega el numero de enemigos restantes en el tablero
	 * @return numero de enemigos restantes en el tablero
	 */
	public int getEnemyTotalNumber()
	{
		return  (currentLocationEnemies.size()+ dungeonEnemies.size());
	}
	/**
	 * Retorna el numero de casillas no visitadas
	 * @return numero de casillas no visitadas
	 */
	public int getNumberOfNotVisitedTiles()
	{
		return notVisited.size();
	}
	public String getPlayerName()
	{
		return player.getName();
	}
	/**
	 * Clase wrapper para Player.isHeDead()
	 * Nos dice si el jugador esta muerto
	 * @return true si el jugador esta muerto
	 */
	public boolean isPlayerDead()
	{
		return player.isHeDead();
	}
	public List<Item> getPlayerInventory()
	{
		return player.getInventory();
	}
	/**
	 * Clase wrapper de Player.inInventory(String a)
	 * Revisa si el objeto esta en el inventario
	 * @param string Nombre del objeto a buscar.
	 * @return true si el objeto se encuentra en el inventario del jugador
	 */
	public boolean isInPlayerInventory(String string) {
		return player.inInventory(string);
	}
	/**
	 * Metodo implementa un ataque de area, tal como el uso de un objeto ofensivo, donde todos los enemigos de la casilla son afectados
	 * @param damage Daño a efectuar sobre los enemigos
	 */
	public void attackAllCurrentEnemies(PlayerDamage damage) {
		List<Enemy> deadEnemies = new ArrayList<Enemy>();
		for (Enemy enemy : currentLocationEnemies) {
			enemy.Defend(damage);
			if (enemy.isDead()) {
				deadEnemies.add(enemy);
				player.AddExp(enemy);
			}
		}
		for (Enemy enemy : deadEnemies) {
			killEnemy(enemy);
		}
		
	}
	/**
	 * Clase wrapper de Player.useItem(string)
	 * Permite el uso de un objeto dentro del inventario
	 * @param string Nombre del objeto a utilizar
	 * @return Si el item no se puede usar, retorna un paquete damage de tipo 'e' y valor 
	 * <p>	-1 => si el item no es del tipo correcto (defensivo, ofensivo, curativo),
	 * <p>	-2 => si la salud esta al maximo y se uso un item curativo,
	 * <p>	-3 => si ya se encuentra bajo los efectos de un objeto defensivo y se utilizo un elemento defensivo
	 * <p>
	 * <p>Si la accion se completo con exito, retorna
	 * <p>	null => si se utilizo un objeto curativo o defensivo
	 * <p>	paquete damage de tipo "o" => si se utilizo objeto ofensivo
	 */
	public PlayerDamage usePlayerItem(String string) {
		return player.useItem(string);
	}
	/**
	 * Permite que el usuario descarte un objeto de su inventario, el cual queda en la casilla actual.
	 * @param argument Nombre del objeto a descartar
	 */
	public void playerDropItem(String argument) {
		Item item = player.dropItem(argument);
		item.setLocation(currentPosition);
		currentLocationItems.add(item);		
	}
	/**
	 * Revisa si un objeto dado esta en la presente casilla
	 * @param itemName	Nombre del objeto dado	
	 * @return true si esta, falsi si no lo esta.
	 */
	public boolean itemIsInCurrent(String itemName){
		boolean returning = false;
		for (Item item : currentLocationItems) {
			if (itemName.equals(item.getName())) {
				returning = true;
				break;
			} 
		}
		return returning;
	}
	/**
	 * Permite que el usuario tome un objeto desde la casilla actual al inventario
	 * @param argument nombre del objeto a recoger.
	 * @return true si se realiza con exito
	 */
	public boolean playerPickItem(String argument) {
		Item item = findItem(argument);
		boolean state = player.addItemToInventory(item);
		if (state) {
			currentLocationItems.remove(item);
			item.setLocation(new Point(-1, -1));
		}
		
		return state;
		
	}
	/**
	 * Permite buscar un objeto en la casilla actual
	 * @param argument nombre del objeto
	 * @return	el objeto en si
	 */
	private Item findItem(String argument) {
		Item returning = null;
		for (Item item : currentLocationItems) {
			if (argument.equals(item.getName())) {
				returning = item;
				break;
			}
		}
		return returning;
	}
	/**
	 * Clase wrapper de Player.equip(string)
	 * Permite que el usuario equipe armaduras y armas.
	 * @param argument nombre del objeto a equipar
	 * @return si la operacion fue exitosa retorna true
	 */
	public boolean playerEquip(String argument) {
		return player.Equip(argument);
	}
	/**
	 * Permite si cierto enemigo existe en la casilla actual
	 * @param argument nombre del enemigo
	 * @return	true si el enemigo esta en la casilla actual
	 */
	public boolean enemyInCurrent(String argument) {
		boolean returning = false;
		for (Enemy enemy : currentLocationEnemies) {
			if (argument.equals(enemy.getName())) {
				returning = true;
				break;
			} 
		}
		return returning;
	}
	/**
	 * Permite buscar un enemigo en la casilla actual
	 * @param argument nombre del enemigo
	 * @return	el enemigo en si
	 */
	public Enemy findEnemy(String argument){
		Enemy returning = null;
		for (Enemy enemy : currentLocationEnemies) {
			if (argument.equals(enemy.getName())) {
				returning = enemy;
				break;
			}
		}
		return returning;
	}
	/**
	 * Implementa el ataque del jugador a UN enemigo dentro de la casilla presente
	 * @param argument nombre del enemigo
	 */
	public void playerAtackEnemy(String argument) {
		PlayerDamage damage = player.Attack();
		Enemy enemy = findEnemy(argument);
		enemy.Defend(damage);
		if (enemy.isDead()) {
			killEnemy(enemy);
			player.AddExp(enemy);
		}		
	}
	/**
	 * Remueve al enemigo de la casilla, puesto que esta muerto
	 * @param enemy El enemigo en si
	 */
	private void killEnemy(Enemy enemy){
		currentLocationEnemies.remove(enemy);		
	}
	/**
	 * Implenta el turno de los enemigos, permitiendo atacar al jugador segun corresponda
	 * @param lastCommand ultimo comando ingresado por el usuario.
	 */
	public void EnemyTurn(String lastCommand) {
		for (Enemy enemy : currentLocationEnemies) {
			if (enemy.isAttacked()||lastCommand.equals("correrMovimiento")||lastCommand.equals("tomar")) {
				Damage damage = enemy.Attack(lastCommand);
				player.Defend(damage);
			}
			if(player.isHeDead()){
				System.out.printf(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_FAINT)+"\n", player.getName());
				break;
			}
		}		
	}

	

}
