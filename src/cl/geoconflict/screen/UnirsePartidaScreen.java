package cl.geoconflict.screen;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class UnirsePartidaScreen extends Screen {
	
	Client client;
	GameStates gamestates;
	Pixmap timePx;
	ArrayList<String> team;
	
	public UnirsePartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
		
		Assets.tenMinRed.scale(70, 70);
	    Assets.fifteenMinRed.scale(70, 70);
	    Assets.twentyMinRed.scale(70, 70);
	    Assets.fifteenMinBlack.scale(70, 70);
	    Assets.hughLayerRed.scale(280, 70);
	    timePx = Assets.tenMinRed;
	
	    team = null;
	}

	@Override
	public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                //back
            	if(inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight()) ) {
//                    if(Settings.soundEnabled)
            		this.game.setScreen(new MenuScreen(this.game, this.client, this.gamestates));
            	}
            }
        }
		
        //define icono de tiempo
        if(gamestates.timeMatch == 10) timePx = Assets.tenMinRed;   
        if(gamestates.timeMatch == 15) timePx = Assets.fifteenMinRed;
        if(gamestates.timeMatch == 20) timePx = Assets.twentyMinRed;
	
        //carga equipo en layers
        if(gamestates.newUpdateRoom){
        	//obtiene equipo para la ventana
        	team = gamestates.getTeam();
        	gamestates.newUpdateRoom = false;
        }
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		
		Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        //muestra nombre de usuario
        g.drawPixmap(Assets.hughLayerRed, 30, 100);
        g.drawText(gamestates.username, 50, 130,Color.WHITE, 30);
        
        if(team != null){
	        //muestra miembros del equipo
	        for(int i = 0; i < team.size(); i++){ //tamaño de equipo
	        	g.drawPixmap(Assets.smallLayerRed, 130, 180+i*50);
	        	g.drawText(team.get(i), 150, 210+ i*50,Color.WHITE, 30);//nombre de jugadore equipo
	        }
        }
        
        //back
        g.drawPixmap(Assets.back,10, 400);
        //tiempo
        g.drawPixmap(timePx, 190, 390);
        //texto de esperar partida
        g.drawText("Espere a que empieze ", 20, 360,Color.BLACK, 30);
        g.drawText("la partida", 20, 380,Color.BLACK, 30);
        //titulo
        g.drawText("Partida", 10, 50, Color.BLACK, 50);
        //nombre de la sala
        g.drawText("'"+gamestates.currMatch+"'", 70, 90, Color.BLACK, 30);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
