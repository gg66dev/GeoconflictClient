package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.animation.Animation;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Player;
import cl.geoconflict.network.Network.*;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class ArmaScreen extends Screen {

	MapaScreen mapa;
	
	Animation arma;
	Clock clockMatch;
	Player player;
	GameStates gameStates;
	Client client;
	
	public ArmaScreen(Game game, Client client, GameStates gameStates) {
		super(game);
		this.client = client;
		this.gameStates = gameStates;
		Assets.animationArma.scale(4900,450);
		arma = new Animation(Assets.animationArma,175,450,27);
				
		Assets.ammo.scale(90,200);
		Assets.lifeplayer.scale(10,50);
		Assets.radarSimple.scale(70,70);
	}
	
	public void setMapaScreen(MapaScreen mapascreen, Clock clock, Player player) {
		this.mapa = mapascreen; 
		this.clockMatch = clock;
		this.player = player;
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		//disminuye tiempo
		this.clockMatch.update(deltaTime);
		
		//actualiza orientacion
		this.player.getDirection();
		
		//cuando se actualiza GPS se envia long y lat al servidor
		this.player.updatePosition();
		
		int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            
            	if(inBounds(event, 190, 290 , 90, 200) && arma.isStop()) {
            		            		
            		if(player.isLoaded()) {
            			arma.shoot();
            			player.addScore(10);
            			player.restAmmo();
            			
            			//envio de posicion y orientacion al servidor
            			RequestShoot rs = new RequestShoot();
            			rs.nameRoom = this.gameStates.currMatch;
            			rs.shootInfo = this.player.getShootInfo();
            			client.sendUDP(rs);
            		}
            	}
            	if(inBounds(event, 0, 400 , 70, 70)) {
            		game.setScreen(mapa);
            	}
            
            }
        }
        
        arma.update(deltaTime);
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
        
        //fondo negro 
		g.drawRect(0, 0, g.getWidth()+10, g.getHeight()+10, Color.BLACK);
		
		//arma
		arma.draw(g);
		
		//puntaje
		g.drawText("puntaje", 180, 30, Color.WHITE, 20);
		g.drawText(player.getScore(), 190, 60, Color.WHITE, 40);
		
		//vida
		g.drawText("vida", 190, 120, Color.WHITE, 20);
		for(int i = 0; i < player.getHealth(); i++)
			g.drawPixmap(Assets.lifeplayer, 190 + (i*15), 130);
		
		//tiempo
		g.drawText("tiempo", 200, 210, Color.WHITE, 20);
		g.drawText(clockMatch.getTime(), 200, 250, Color.WHITE, 40);
		
		//municion
		g.drawPixmap(Assets.ammo, 190, 290);
		g.drawText(player.getAmmo(), 210, 340, Color.WHITE, 50);
		
        //radar simple
        g.drawPixmap(Assets.radarSimple, 0, 400);
		
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
