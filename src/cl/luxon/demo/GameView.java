/**
 * 
 */
package cl.luxon.demo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** Manejo gr&aacute;fico del <i>tablero</i> del juego
 * @author Fernando Valencia F.
 *
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	private GameLoopThread gameLoopThread;
	private GPS gps;
	private Player player;
	private Map map;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		getHolder().addCallback(this);
		
		this.gps = new GPS(context);
		this.map = new Map();
		this.gps.addObserver(this.map);
		this.gps.activarGPS(true);
	}

	/** Actualiza el estado visual del juego
	 * 
	 * @param canvas <i>Pizarr&oacute;n</i> donde se dibuja el juego
	 */
	public void update(Canvas canvas){
		canvas.drawColor(Color.BLACK);
	    player.onDraw(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/* se instancia un sprite */
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), cl.geoconflict.R.drawable.punto_blanco);
		player = new Player(bmp, this, this.map, this.gps);
		
		/* se inicia el loop del juego */
		gameLoopThread.setRunning(true);
        gameLoopThread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e("surfaceDestruyed", "Hilo detenido");
		
		boolean retry = true;
		gameLoopThread.setRunning(false);
		while(retry){
			try{
				gameLoopThread.join();
				retry = false;
				
			}catch(InterruptedException e){
				//TODO depuracion
			}
		}
	}
}
