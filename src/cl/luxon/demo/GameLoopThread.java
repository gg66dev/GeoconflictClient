/**
 * 
 */
package cl.luxon.demo;

import android.graphics.Canvas;

/** Bucle l&oacute;gico del juego
 * @author Fernando Valencia F.
 *
 */
public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;
    static final long FPS = 10;
   
    /** Constructor del bucle l&oacute;gico del juego
     * 
     * @param view <i>Tablero</i> a controlar
     */
    public GameLoopThread(GameView view) {
          this.view = view;
    }

    /** Setter que define el estado del juego
     * 
     * @param run True si el juego esta <i>corriendo</i>, False 
     * en caso contrario
     */
    public void setRunning(boolean run) {
          running = run;
    }

    /** Hilo que maneja el bucle */
    @Override
    public void run() {
    	long ticksPS = 1000 / FPS;
    	/* tiempos de comienzo del loop y de pause respectivamente */
        long startTime, sleepTime;
          while (running) {
                 Canvas c = null;
                 startTime = System.currentTimeMillis();
                 try {
                        c = view.getHolder().lockCanvas();
                        synchronized (view.getHolder()) {
                               view.update(c);
                        }
                 } finally {
                        if (c != null) {
                               view.getHolder().unlockCanvasAndPost(c);
                        }
                 }
                 sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
                 try {
                        if (sleepTime > 0)
                        	sleep(sleepTime);
                        else{
                        	sleep(10);
                        }
                 } catch (Exception e) {}
          }
    }
}  
