/**
 * 
 */
package cl.geoconflict.gameplay;

import com.esotericsoftware.minlog.Log;

/**
 * @author Fernando Valencia F.
 *
 */
public class Map{
	
	private final static double WIDTH = 50d;
	private final static double HEIGHT = 100d;
	private int screenHeight;
	private int screenWidth;
	private final static int FACTOR = 1;
	
	public Map( int screenHeight, int screenWidth){
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}
	
	public Position getPosition( double x, double y){
		Position pos = new Position();
		Log.debug("(x, y): (" + x + ", " + y + " ) ");
//		pos.x = ( screenWidth/2 + (x * Map.FACTOR) * screenWidth/Map.WIDTH );
//		pos.y = ( screenHeight/2 + (y * Map.FACTOR) * screenHeight/Map.HEIGHT );
		pos.x = ( (x * Map.FACTOR) * screenWidth/Map.WIDTH );
		pos.y = ( (y * Map.FACTOR) * screenHeight/Map.HEIGHT );
		return pos;
	}
}
