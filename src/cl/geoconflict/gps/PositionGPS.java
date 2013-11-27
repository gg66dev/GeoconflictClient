/**
 * 
 */
package cl.geoconflict.gps;

import com.badlogic.androidgames.framework.Game;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import android.util.Log;

/**
 * @author Fernando Valencia F.
 *
 */
public class PositionGPS extends Position {
	

	Game game;
	/** Constructor del GPS
	 * 
	 * @param context el ambiente de la aplicaci&oacute;n
	 */
	public PositionGPS(Game game){
		super();
		this.game = game;
		
	}
	
//	/** M&eacute;todo que permite iniciar o desactivar la actualizaci&oacute;n 
//	 * de datos del GPS
//	 * 
//	 * @param activar para activar/desactivar el gps
//	 */
//	public void activarGPS(boolean activar){
//		if(activar)
//			game.getInput().requestUpdate(0, 0);
//		else
//			// Se elimina el listener
//			game.getInput().removeUpdate();
//	}

	//se llama igual que la clase de LocationListener
	//funciona igual pero no pertenece a la interfaz
	public void onLocationChanged() {
		//variable interna indica cambio de posicion
		if(game.getInput().isLocationChanged()){
			boolean setMapCoords = false;
			if( this.obs != null && latitud == 0 && longitud == 0){
				setMapCoords = true;
			}
			// si la posicion cambia se obtienen las nuevas coordenadas
			latitud = game.getInput().getLatitud();
			longitud = game.getInput().getLongitud();
			Log.d("debug","lat"+latitud);
			Log.d("debug","log"+longitud);
			
			LatLng position = new LatLng(latitud, longitud);
			UTMRef utmRef = position.toUTMRef();
			this.x = utmRef.getEasting();
			this.y = utmRef.getNorthing();
			
			if(setMapCoords){
				for(Observer o : this.obs)
					o.onInitialPosition((int) this.x, (int) this.y);
			}
			//asigana variable interna de listener false
			game.getInput().notLocationChanged();
		}
	}

}
