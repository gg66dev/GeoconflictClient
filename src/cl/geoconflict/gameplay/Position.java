/**
 * 
 */
package cl.geoconflict.gameplay;

/** Posici&oacute;n de un jugador en el mapa
 * @author Fernando Valencia F.
 *
 */
public class Position {

	/** coordenada x de la posici&oacute;n */
	public double x;
	/** coordenada y de la posici&oacute;n */
	public double y;
	
	public Position(){
		this.x = 0d;
		this.y = 0d;
	}
	
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	
}
