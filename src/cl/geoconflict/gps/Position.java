/**
 * 
 */
package cl.geoconflict.gps;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fernando Valencia F.
 *
 */
public abstract class Position{

	protected double latitud;
	protected double longitud;
	protected double x;
	protected double y;
	
	/** un observador */
	protected List<Observer> obs;
	
	/** Constructor de la clase
	 * 
	 */
	public Position(){
		this.obs = new ArrayList<Observer>();
		this.latitud = 0;
		this.longitud = 0;
		this.x = 0;
		this.y = 0;
	}
	/**
	 * @return the latitud
	 */
	public double getLatitud(){
		return this.latitud;
	}
	/**
	 * @return the longitud
	 */
	public double getLongitud(){
		return this.longitud;
	}
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	/**
	 * add the observer
	 */
	public void addObserver(Observer obs){
		this.obs.add(obs);
	}
	
}
