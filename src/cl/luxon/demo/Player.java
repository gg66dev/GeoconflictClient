/**
 * 
 */
package cl.luxon.demo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/** Clase que implementa un jugador y hereda la parte gr&aacute;fica del <i>sprite</i> correspondiente
 * @author Fernando Valencia F.
 * 
 */
public class Player extends Sprite {

	/** Le otorga mayor sensibilidad al GPS, haciendo que el movimiento se vea m&aacute;s r&aacute;pido */
	public static final int SPEED = 3;
	
	private int health;
	private GameView view;
	private Map map;
	private GPS gps;
	
	/** Constructor de la clase, posiciona al jugador al medio del mapa
	 * @param bmp Una imagen
	 */
	public Player(Bitmap bmp, GameView view, Map map, GPS gps) {
		super(bmp);
		this.health = 10;
		this.view = view;
		this.map = map;
		this.gps = gps;
		this.x = view.getWidth()/2 - this.width/2;
		this.y = view.getHeight()/2 - this.height/2;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/** Actualiza el movimiento del jugador en el juego */
	private void update(){
		if( this.gps.x != 0 || this.gps.y != 0){
			this.x = (int) (this.view.getWidth()/2 - this.width/2 +
					( ( (int) this.gps.x ) - this.map.getInitialX()) * SPEED );
			this.y = (int) (this.view.getHeight()/2 - this.height/2 + 
					( ( (int) this.gps.y ) - this.map.getInitialY()) * SPEED );
		}
	}
	
	/** Dibuja al jugador en pantalla, hereda comportamiento de su <i>Sprite</i> y adem&aacute;s actualiza 
	 * su posici&oacute;n */
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		update();
	}

}
