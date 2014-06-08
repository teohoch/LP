import java.awt.Point;
import java.io.IOException;

import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader;
import cl.utfsm.inf.lp.sem12014.mud.input.MessageReader.MessageCode;
import dungeon.Dungeon;



public class Zorg {
	

	public static void main(String[] args) {
		MessageReader messenger = null;
		try {
			messenger = new MessageReader("messages.mud");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Dungeon laberinth = new Dungeon("enemies-bonus.mud", "items.mud", "board.mud", "player.mud",messenger);
		
		System.out.println(messenger.getMessage(MessageCode.MESSAGE_GAME_ATTACK_DAMAGE));
		System.out.printf(MessageCode.MESSAGE_GAME_ITEM_DEFENSE_FULL.toString());
		System.out.println("hola!");
		
		
		

	}

}
