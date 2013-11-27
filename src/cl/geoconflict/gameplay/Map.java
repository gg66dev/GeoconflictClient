/**
 * 
 */
package cl.geoconflict.gameplay;

import cl.geoconflict.gps.Observer;

/**
 * @author Fernando Valencia F.
 *
 */
public class Map implements Observer{
	
	private double initialX;
	private double initialY;
	/**
	 * @param initialX
	 * @param initialY
	 */
	public Map() {}
	/**
	 * @return the initialX
	 */
	public double getInitialX() {
		return initialX;
	}
	/**
	 * @return the initialY
	 */
	public double getInitialY() {
		return initialY;
	}
	@Override
	public void onInitialPosition(int x, int y) {
		this.initialX = x;
		this.initialY = y;
	}
	
}
