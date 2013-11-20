/**
 * 
 */
package cl.luxon.demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/** Clase encargada de dibujar los <i>sprites</i> en pantalla
 * @author Fernando Valencia F.
 *
 */
public abstract class Sprite {
	/** N&uacute;mero de filas la imagen del <i>sprite</i> */
	private static final int BMP_ROWS = 1;
	/** N&uacute;mero de columnas en la imagen del <i>sprite</i>*/
    private static final int BMP_COLUMNS = 3;
    
    /* posicion del sprite */
    protected int x = 0;
    protected int y = 0;
    
    /* una imagen */
    private Bitmap bmp;
    
    /* actual cuadro del sprite */
    private int currentFrame = 0;
    
    /* ancho y alto */
    protected int width;
    protected int height;

    /** Constructor de los <i>sprites</i> a utilizar en el juego
     *  
     * @param bmp Imagen del sprite a dibujar en la pantalla
     */
    public Sprite(Bitmap bmp) {
          this.bmp = bmp;
          this.width = bmp.getWidth() / BMP_COLUMNS;
          this.height = bmp.getHeight() / BMP_ROWS;
    }

	/** Actualiza la animaci&oacute;n y movimiento del juego */
    private void update() {
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    /** realiza el refresco de la animaci&oacute;n del <i>Sprite</i> en el juego */
    @SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
          update();
          int srcX = currentFrame * width;
          int srcY = 0 * height;
          Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
          Rect dst = new Rect(x, y, x + width, y + height);
          canvas.drawBitmap(bmp, src, dst, null);
    }
    
    /** Indica si un punto en particular est&aacute; dentro de los m&aacute;rgenes de este sprite.
	 * 
	 * @param point Punto a revisar.
	 * @return {@code true} si es que el punto est&aacute; dentro de los m&aacute;rgenes de este sprite, {@code false} en caso contrario.
	 */
	public boolean containsPoint(Point point){
		return ((this.x <= point.x && this.y + this.width >= point.x) && (this.y <= point.y && this.y + this.height >= point.y));
	}
}  
