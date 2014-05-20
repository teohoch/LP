import java.awt.Point;
import java.io.IOException;

import cl.utfsm.inf.lp.sem12014.mud.input.EnemyReader;
import cl.utfsm.inf.lp.sem12014.mud.input.ItemReader;

import java.util.ArrayList;
import java.util.List;

import utils.Item;


public class Zorg {
	public static List<Enemy> LoadEnemies(String EnemiesPath)
	{
		EnemyReader<Enemy> eReader = null;
		try {
			eReader = new EnemyReader<Enemy>(EnemiesPath, Enemy.class);
		} catch (IOException e) {
			System.out.println("Error en la lectura de Enemigos! Terminando programa");
			e.printStackTrace();			
		}
		List<Enemy> EnemiesList = null;
		try {
			EnemiesList = eReader.getEnemies();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		return EnemiesList;
		
	}

	public static void main(String[] args) {
		
		List<Enemy> enemiesList = LoadEnemies("enemies-bonus.mud");
		
		for (Enemy enemy : enemiesList) {
			enemy.println();
		}
		
		String itemPath = "items.mud";
		ItemReader iReader = null;
		try {
			iReader = new ItemReader(itemPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Item> dungeonItems = new ArrayList<Item>();
		String items[] = iReader.getKeys();
		for (String item : items) {
			char type = iReader.getType(item);
			Point location = iReader.getPosition(item);
			int value = iReader.getValue(item);
			Item Temp = new Item(item, type, value, location);
			dungeonItems.add(Temp);			
		}
		for (Item item : dungeonItems) {
			item.println();
		}
		
		
		

	}

}
