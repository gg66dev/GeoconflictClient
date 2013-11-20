package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.MenuScreen;
import cl.geoconflict.gui.ButtonGUI;
import cl.geoconflict.gui.ScrollBar;
import cl.geoconflict.network.Network.RequestCloseRoom;
import cl.geoconflict.network.Network.RequestCreateRoom;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class CrearPartidaScreen extends Screen {

	ScrollBar  scrollbar;
	Client client;
	GameStates gamestates;
	
	public CrearPartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
	
		scrollbar = new ScrollBar(10, 90, 300, 170);
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
		scrollbar.add(new ButtonGUI(Assets.mediumLayerBlack,255,50));
		scrollbar.add(new ButtonGUI(Assets.mediumLayerBlack,255,50));
		scrollbar.add(new ButtonGUI(Assets.mediumLayerBlack,255,50));
		
		//le pone el nombre player a los botones 
		for(int i = 0; i < scrollbar.size(); i++){
			
			scrollbar.GetElement(i).setDraw(true);
			
			if(i == 0)
				scrollbar.GetElement(i).setName(this.gamestates.username);
			else
				scrollbar.GetElement(i).setName("Jugador"+i);
		}
		
	}

	@Override
	public void update(float deltaTime) {
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	        
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
			if(inBounds(event, 10, 90, 400, 170) ) {
	            //desplaza posicion del scrollbar
	            scrollbar.update(deltaTime,event);
	        }
			if(inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight()) ) {
				RequestCloseRoom rcr =  new RequestCloseRoom();
            	rcr.userNameRoom = this.gamestates.username;
            	this.client.sendTCP(rcr);
            	this.gamestates.roomAcepted = false;
				this.game.setScreen(new MenuScreen(this.game, this.client,this.gamestates));
			}
			if(inBounds(event, 150, 400, Assets.empezar.getWidth(), Assets.empezar.getHeight()) ) {
				this.game.setScreen(new ArmaScreen(this.game, this.client,this.gamestates));
			}
	    }
		
	}

	
	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        
        //escala las images
        Assets.tenMinBlack.scale(70, 70);
        Assets.fifteenMinBlack.scale(70, 70);
        Assets.twentyMinBlack.scale(70, 70);
        Assets.sdUp.scale(70, 70);
        Assets.empezar.scale(170,64);
        
        scrollbar.draw(g);
        
        g.drawPixmap(Assets.tenMinBlack, 90, 320);
        g.drawPixmap(Assets.fifteenMinBlack, 170, 320);
        g.drawPixmap(Assets.twentyMinBlack, 250, 320);
        g.drawPixmap(Assets.sdUp, 10, 320);
        g.drawPixmap(Assets.empezar,150, 400);
        g.drawPixmap(Assets.back,10, 400);
        
        g.drawText("Crear Partida", 10, 50, Color.BLACK, 50);
		
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
