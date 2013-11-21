package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.MenuScreen;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Player;
import cl.geoconflict.gui.ButtonGUI;
import cl.geoconflict.gui.ScrollBar;
import cl.geoconflict.network.Network.RequestCloseRoom;
import cl.geoconflict.network.Network.RequestCreateRoom;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class CrearPartidaScreen extends Screen {

	ScrollBar  scrollbar;
	Client client;
	GameStates gamestates;
	
	//tiempo
	Pixmap tenMin;
	Pixmap fifteenMin;
	Pixmap twentyMin;
	
	public CrearPartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
	
		//el primer jugador de la partida es el administrador
		scrollbar = new ScrollBar(10, 90, 300, 170);
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
		scrollbar.GetElement(0).setName(this.gamestates.username);
		
		//escala las images
        Assets.tenMinBlack.scale(70, 70);
        Assets.fifteenMinBlack.scale(70, 70);
        Assets.twentyMinBlack.scale(70, 70);
        Assets.tenMinRed.scale(70, 70);
        Assets.fifteenMinRed.scale(70, 70);
        Assets.twentyMinRed.scale(70, 70);
        Assets.sdUp.scale(70, 70);
        Assets.empezar.scale(170,64);
        
		
		//tiempo 
        tenMin = Assets.tenMinRed;
		fifteenMin = Assets.fifteenMinBlack;
		twentyMin = Assets.twentyMinBlack;
		gamestates.timeMatch = 10;
		
	}

	@Override
	public void update(float deltaTime) {
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	        
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        //desplaza posicion del scrollbar
	        if(inBounds(event, 10, 90, 400, 170) ) {
	            scrollbar.update(deltaTime,event);
	        }
			//back
			if(inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight()) ) {
				RequestCloseRoom rcr =  new RequestCloseRoom();
            	rcr.userNameRoom = this.gamestates.username;
            	this.client.sendTCP(rcr);
            	this.gamestates.roomAcepted = false;
				this.game.setScreen(new MenuScreen(this.game, this.client,this.gamestates));
			}
			//empezar partida
			if(inBounds(event, 150, 400, Assets.empezar.getWidth(), Assets.empezar.getHeight()) ) {
				Clock clockMatch = new Clock(15); //p: tiempo partida
        		Player player = new Player(20); //p: ammo
        		ArmaScreen arma = new ArmaScreen(game,client,gamestates);
        		MapaScreen mapa = new MapaScreen(game,client,gamestates);
        		arma.setMapaScreen(mapa,clockMatch,player);
        		mapa.setArmaScreen(arma,clockMatch,player);
        		this.game.setScreen(mapa);
			}
			//10 min
			if(inBounds(event, 90, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				tenMin = Assets.tenMinRed;
				fifteenMin = Assets.fifteenMinBlack;
				twentyMin = Assets.twentyMinBlack;
				gamestates.timeMatch = 10;
			}
			//15 min
			if(inBounds(event, 170, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				tenMin = Assets.tenMinBlack;
				fifteenMin = Assets.fifteenMinRed;
				twentyMin = Assets.twentyMinBlack;
				gamestates.timeMatch = 15;
			}
			//20 min
			if(inBounds(event, 250, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				tenMin = Assets.tenMinBlack;
				fifteenMin = Assets.fifteenMinBlack;
				twentyMin = Assets.twentyMinRed;
				gamestates.timeMatch = 20;
			}
			
			//actualiza lista de player
			if(gamestates.newPlayerList){
				//le asigan al nuevo cualquier equipo
				int n = scrollbar.size();
				scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
				scrollbar.GetElement(n).setName((gamestates.arrayUsers.get(n)));
				n++;
				//reordena equipos automaticamente de forma que quden casi del mismo tamaño
				int j;
				for(j=0; j < (n+1)/2;j++)
					scrollbar.GetElement(j).setPixmapLayer(Assets.mediumLayerRed);
				for(; j < n;j++)
					scrollbar.GetElement(j).setPixmapLayer(Assets.mediumLayerBlack);
				gamestates.newPlayerList = false;
				
				//informa cambios al servidor
				
			}
	    }
		
	}

	
	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        scrollbar.draw(g);
        
        g.drawPixmap(tenMin, 90, 320);
        g.drawPixmap(fifteenMin, 170, 320);
        g.drawPixmap(twentyMin, 250, 320);
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
