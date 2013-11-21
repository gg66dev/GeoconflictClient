package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.animation.PlayerPoint;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Player;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class MapaScreen extends Screen {

	ArmaScreen arma = null;
	Clock clockMatch = null; //p: tiempo partida
	Player player = null; //p: ammo
	
	PlayerPoint playerPoints[];
	int numPlayers = 3;
	
	public MapaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		
		Assets.simpleAmmo.scale(90,90);
		Assets.playerWhite.scale(140, 40);
		Assets.playerGreen.scale(140, 40);
		
		// equipo, 0 es el jugador , 1 y 2 son los compa�eros
		playerPoints = new PlayerPoint[numPlayers];
		playerPoints[0] = new PlayerPoint(Assets.playerWhite, 20, 20, 1, 6);
		playerPoints[1] = new PlayerPoint(Assets.playerGreen, 20, 20, 1, 6);
		playerPoints[2] = new PlayerPoint(Assets.playerGreen, 20, 20, 1, 6);
		
		
	}
	
	public void setArmaScreen(ArmaScreen armascreen, Clock clock, Player player)
	{
		this.arma = armascreen;
		this.clockMatch = clock;
		this.player = player;
	
		Assets.lifeplayer.scale(10,50);
	}
	

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		//se actualiza la posicion de los jugadores
		//deberian ser coordenadas del servidor
		playerPoints[0].setPosition(20,200);
		playerPoints[1].setPosition(200,200);
		playerPoints[2].setPosition(150,300);
		
		//y la animacion
		for(int i = 0; i < numPlayers ; i++)
			playerPoints[i].update(deltaTime);
		
		
		//disminuye tiempo
		clockMatch.update(deltaTime);		
		
		
		int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if(inBounds(event, 0, 400 , 90, 90)) {
            		if (arma != null)
            			game.setScreen(arma);
            	}
            }
        }
		
		
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
        
		g.drawRect(0, 0, g.getWidth()+10, g.getHeight()+10, Color.BLACK);
        
        //mostrar grilla
        Assets.geogrilla.scale(g.getWidth()-30, g.getHeight()-30);
        g.drawPixmap(Assets.geogrilla, 0, 0);
        
        
        //puntaje
        g.drawTextRotate("Puntaje", 50, 100, Color.WHITE,20, 90);
        g.drawTextRotate(player.getScore(), 50, 120, Color.WHITE,20, 90);
        
       
    	//tiempo
    	g.drawTextRotate("Tiempo", -50, 100, Color.WHITE,20, 90);
        g.drawTextRotate(clockMatch.getTime(), -50, 120, Color.WHITE,20, 90);
      
    	
    	//if bonus
      		
      	//vida
      	for(int i = 0; i < player.getLifeCount(); i++)
      		g.drawPixmap(Assets.lifeplayer, 170 + (i*15), 420);
      		
      
      	
      	//mostrar jugadores
      	for(int i = 0; i < numPlayers ; i++)
      		playerPoints[i].draw(g);
        
        //municion
        g.drawPixmap(Assets.simpleAmmo, 0, 400);
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
