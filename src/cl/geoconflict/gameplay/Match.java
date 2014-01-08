/** Clase que se ocupa de la instancia del match
 * 
 */
package cl.geoconflict.gameplay;

import com.badlogic.androidgames.framework.Game;

/**
 * @author Fernando Valencia
 *
 */
public class Match {

	private static Player PLAYER = null;
	private static Match INSTANCE = new Match();
	
	/**
	 * 
	 */
	private Match() {
	}

	public static Match getInstance(){
		return INSTANCE;
	}
	
	public void preparePlayer(Game game){
		PLAYER = new Player(20, game);
	}
	
	public static Player getPlayer(){
		return PLAYER;
	}
}
