/**
 * 
 */
package cl.geoconflict;


import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

/**
 * @author Fernando Valencia F.
 *
 */
public class GeoConflictGame extends AndroidGame {
	
	@Override
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
}
