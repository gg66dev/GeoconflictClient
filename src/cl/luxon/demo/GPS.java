/**
 * 
 */
package cl.luxon.demo;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author Fernando Valencia F.
 *
 */
public class GPS extends Position implements LocationListener{
	
	private Context context;
	private LocationManager lm;

	/** Constructor del GPS
	 * 
	 * @param context el ambiente de la aplicaci&oacute;n
	 */
	public GPS(Context context){
		super();
		this.context = context;
		lm = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	/** M&eacute;todo que permite iniciar o desactivar la actualizaci&oacute;n 
	 * de datos del GPS
	 * 
	 * @param activar para activar/desactivar el gps
	 */
	public void activarGPS(boolean activar){
		if(activar)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		else
			// Se elimina el listener
			lm.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		boolean setMapCoords = false;
		if( this.obs != null && latitud == 0 && longitud == 0){
			setMapCoords = true;
		}
		// si la posicion cambia se obtienen las nuevas coordenadas
		latitud = location.getLatitude();
		longitud = location.getLongitude();
		
		LatLng position = new LatLng(latitud, longitud);
		UTMRef utmRef = position.toUTMRef();
		this.x = utmRef.getEasting();
		this.y = utmRef.getNorthing();
		
		if(setMapCoords){
			for(Observer o : this.obs)
				o.onInitialPosition((int) this.x, (int) this.y);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
