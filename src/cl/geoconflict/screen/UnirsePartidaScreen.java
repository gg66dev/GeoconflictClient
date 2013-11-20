package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.MenuScreen;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class UnirsePartidaScreen extends Screen {
	
	Client client;
	GameStates gamestates;
	
	public UnirsePartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
	}

	@Override
	public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight()) ) {
//                    if(Settings.soundEnabled)
                	this.game.setScreen(new MenuScreen(this.game, this.client, this.gamestates));
                }
            }
        }
		
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		
		Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        //g.drawPixmap(Assets.logo, 32, 28);
        
        Assets.twentyMinBlack.scale(70, 70);
        Assets.twentyMinBlack.scale(70, 70);
        Assets.twentyMinBlack.scale(70, 70);
        Assets.fifteenMinBlack.scale(70, 70);
        Assets.hughLayerRed.scale(280, 70);
        
        g.drawPixmap(Assets.hughLayerRed, 30, 100);
        g.drawPixmap(Assets.smallLayerRed, 130, 180);
        g.drawPixmap(Assets.smallLayerRed, 130, 230);
        g.drawPixmap(Assets.smallLayerRed, 130, 280);
        g.drawText("Jugador1", 50, 130,Color.WHITE, 30);
        g.drawText("Jugador2", 150, 210,Color.WHITE, 20);
        g.drawText("Jugador3", 150, 260,Color.WHITE, 20);
        g.drawText("Jugador4", 150, 310,Color.WHITE, 20);
        
        //texto de esperar partida
        
        g.drawPixmap(Assets.back,10, 400);
        g.drawPixmap(Assets.fifteenMinBlack, 190, 390);
        
        g.drawText("Espere a que empieze ", 20, 360,Color.BLACK, 30);
        g.drawText("la partida", 20, 380,Color.BLACK, 30);
        
        g.drawText("Partida", 10, 50, Color.BLACK, 50);
        g.drawText("'Nombre Adm'", 70, 90, Color.BLACK, 30);
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
