package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import android.util.Log;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Map;
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
	GameStates gamestates;
	
	//tiempo
	Pixmap tenMin;
	Pixmap fifteenMin;
	Pixmap twentyMin;
	
	public CrearPartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
	
		scrollbar = new ScrollBar(10, 90, 300, 170);
		
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
            	break;
            }
			//empezar partida
			if(inBounds(event, 150, 400, Assets.empezar.getWidth(), Assets.empezar.getHeight()) ) {
				RequestMatchInit rmi = new RequestMatchInit();
				//no se envian los datos de la partida por que ya estan en el servidor
				rmi.nameRoom = gamestates.username;
				rmi.origin = gamestates.getOrigin();
				client.sendTCP(rmi);
			}
			//cambio se realiza primero en el servidor
			//gamestataes cambia cuando recive la confirmacion
			//10 min
			if(inBounds(event, 90, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				Log.d("debug","se preciono 10");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 10;
				rct.nameRoom = gamestates.username;
				client.sendTCP(rct);
			}
			//15 min
			if(inBounds(event, 170, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				Log.d("debug","se preciono 15");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 15;
				rct.nameRoom = gamestates.username;
				client.sendTCP(rct);
			}
			//20 min
			if(inBounds(event, 250, 320,Assets.tenMinBlack.getWidth(),Assets.tenMinBlack.getHeight())){
				Log.d("debug","se preciono 20");
				RequestChangeTime rct = new RequestChangeTime();
				rct.timeMatch =""+ 20;
				rct.nameRoom = gamestates.username;
				client.sendTCP(rct);
			}
		}
	    
	    //nuevo player ingreso
		//actualiza lista de player
		if(gamestates.newUpdateRoom){
			scrollbar.clear();
			int i = 0;
			if(gamestates.teamRed.size() > 0){
				for( String p : gamestates.teamRed ){
					scrollbar.add(new ButtonGUI(Assets.mediumLayerRed,255,50));
					scrollbar.GetElement(i).setName(p);
					Log.d("debug","agregado "+p);
					i++;
				}
			}
			if(gamestates.teamBlack.size() > 0){
				for( String p : gamestates.teamBlack ){
					scrollbar.add(new ButtonGUI(Assets.mediumLayerBlack,255,50));
					scrollbar.GetElement(i).setName(p);
					Log.d("debug","agregado "+p);
					i++;
				}
			}
			
			//deja de ser una nueva lista
			gamestates.newUpdateRoom = false;
		}
		
		//cambio de tiempo
		if(gamestates.timeChange){
			if(gamestates.timeMatch == 10){
				tenMin = Assets.tenMinRed;
				fifteenMin = Assets.fifteenMinBlack;
				twentyMin = Assets.twentyMinBlack;
			}
			if(gamestates.timeMatch == 15){
				tenMin = Assets.tenMinBlack;
				fifteenMin = Assets.fifteenMinRed;
				twentyMin = Assets.twentyMinBlack;
			}
			if(gamestates.timeMatch == 20){
				tenMin = Assets.tenMinBlack;
				fifteenMin = Assets.fifteenMinBlack;
				twentyMin = Assets.twentyMinRed;
			}
			gamestates.timeChange = false;
		}
			
		
		
		//se inicia la partida
		if(gamestates.initMatch){
			//se crea mapa se asocia a Player y a gps
			Map map = new Map();
			gamestates.gps.addObserver(map);
			Clock clockMatch = new Clock(15); //p: tiempo partida
			Player player = new Player(20,map,gamestates.gps); //p: ammo,mapa, gps
    		ArmaScreen arma = new ArmaScreen(game,client,gamestates);
    		MapaScreen mapa = new MapaScreen(game,client,gamestates);
    		arma.setMapaScreen(mapa,clockMatch,player);
    		mapa.setArmaScreen(arma,clockMatch,player);
    		this.game.setScreen(mapa);
		}
		// cierre de partida, salen a mainMenu
		if (gamestates.closeRoom) {
			this.gamestates.roomAcepted = false;
			this.game.setScreen(new MenuScreen(this.game, this.client,this.gamestates));
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
