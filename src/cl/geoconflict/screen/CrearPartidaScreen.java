package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import android.util.Log;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Player;
import cl.geoconflict.gui.ButtonGUI;
import cl.geoconflict.gui.ScrollBar;
import cl.geoconflict.network.Network.RequestCloseRoom;
import cl.geoconflict.network.Network.RequestMatchInit;
import cl.geoconflict.network.Network.RequestChangeTime;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class CrearPartidaScreen extends Screen {

	ScrollBar  scrollbar;
	Client client;
	GameStates gameStates;
	Player player;
	
	//tiempo
	Pixmap tenMin;
	Pixmap fifteenMin;
	Pixmap twentyMin;
	
	public CrearPartidaScreen(Game game, Client client, GameStates gameStates, Player player) {
		super(game);
		this.client = client;
		this.gameStates = gameStates;
		this.player = player;
	
		this.scrollbar = new ScrollBar(10, 90, 300, 170);
		
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
		gameStates.timeMatch = 10;
		
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
	        }else if(inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight()) ) {
				//back
				RequestCloseRoom rcr =  new RequestCloseRoom();
            	rcr.userNameRoom = this.gameStates.username;
            	this.client.sendTCP(rcr);
            	break;
            }else if(inBounds(event, 150, 400, Assets.empezar.getWidth(), Assets.empezar.getHeight()) ) {
            	//empezar partida
				RequestMatchInit rmi = new RequestMatchInit();
				//no se envian los datos de la partida por que ya estan en el servidor
				rmi.nameRoom = gameStates.username;
				rmi.origin = this.player.getPosition();
				this.client.sendTCP(rmi);
			}else if(inBounds(event, 90, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				//cambio se realiza primero en el servidor
				//gamestataes cambia cuando recive la confirmacion
				//10 min
				Log.d("debug","se preciono 10");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 10;
				rct.nameRoom = gameStates.username;
				this.client.sendTCP(rct);
			}else if(inBounds(event, 170, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				//15 min
				Log.d("debug","se preciono 15");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 15;
				rct.nameRoom = gameStates.username;
				this.client.sendTCP(rct);
			}else if(inBounds(event, 250, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				//20 min
				Log.d("debug","se preciono 20");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 20;
				rct.nameRoom = this.gameStates.username;
				this.client.sendTCP(rct);
			}
		}
	    
	    //nuevo player ingreso
		//actualiza lista de player
		if( this.gameStates.newUpdateRoom ){
			this.scrollbar.clear();
			int i = 0;
			if( this.gameStates.teamRed.size() > 0 ){
				for( String p : this.gameStates.teamRed ){
					this.scrollbar.add(new ButtonGUI( Assets.mediumLayerRed, 255, 50 ));
					this.scrollbar.GetElement(i).setName(p);
					Log.d("debug","agregado "+p);
					i++;
				}
			}
			if( this.gameStates.teamBlack.size() > 0 ){
				for( String p : this.gameStates.teamBlack ){
					scrollbar.add( new ButtonGUI( Assets.mediumLayerBlack, 255, 50 ) );
					scrollbar.GetElement(i).setName(p);
					Log.d("debug","agregado "+p);
					i++;
				}
			}
			
			//deja de ser una nueva lista
			this.gameStates.newUpdateRoom = false;
		}
		
		//cambio de tiempo
		if( this.gameStates.timeChange){
			if( this.gameStates.timeMatch == 10){
				this.tenMin = Assets.tenMinRed;
				this.fifteenMin = Assets.fifteenMinBlack;
				this.twentyMin = Assets.twentyMinBlack;
			}else if(gameStates.timeMatch == 15){
				this.tenMin = Assets.tenMinBlack;
				this.fifteenMin = Assets.fifteenMinRed;
				this.twentyMin = Assets.twentyMinBlack;
			}else if(gameStates.timeMatch == 20){
				this.tenMin = Assets.tenMinBlack;
				this.fifteenMin = Assets.fifteenMinBlack;
				this.twentyMin = Assets.twentyMinRed;
			}
			this.gameStates.timeChange = false;
		}
			
		//se inicia la partida
		if( this.gameStates.initMatch ){
			Clock clockMatch = new Clock(15); //p: tiempo partida
    		ArmaScreen arma = new ArmaScreen( this.game, this.client, this.gameStates);
    		MapaScreen mapa = new MapaScreen( this.game, this.client, this.gameStates);
    		arma.setMapaScreen( mapa, clockMatch, this.player);
    		mapa.setArmaScreen( arma, clockMatch, this.player);
    		this.game.setScreen(mapa);
		}
		// cierre de partida, salen a mainMenu
		if ( this.gameStates.closeRoom ) {
			this.gameStates.roomAcepted = false;
			this.gameStates.closeRoom = false;
			this.game.setScreen(new MenuScreen(this.game, this.client,this.gameStates));
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
