package cl.geoconflict;

import java.io.IOException;

import org.apache.sling.commons.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import cl.geoconflict.network.Network;
import cl.geoconflict.network.NetworkListener;
import cl.geoconflict.network.Network.RequestChangeTime;
import cl.geoconflict.network.Network.RequestCloseRoom;
import cl.geoconflict.network.Network.RequestCreateRoom;
import cl.geoconflict.network.Network.RequestJoinRoom;
import cl.geoconflict.network.Network.RequestLeaveRoom;
import cl.geoconflict.network.Network.RequestListRoom;
import cl.geoconflict.network.Network.RequestLogin;
import cl.geoconflict.network.Network.RequestMatchInit;
import cl.geoconflict.network.Network.RequestRegisterUser;
import cl.geoconflict.network.Network.RequestRoomUpdate;

public class ConnectionManager {

	static Thread connectionLoop;

	static boolean startConnection = false;
	static boolean sendRegister = false;
	static boolean isConnected = false;
	static boolean sendLogin = false;
	static boolean createMatch = false;
	static boolean listMatch = false;
	static boolean closeRoom = false;
	static boolean changeTime = false;
	static boolean roomUpdate = false;
	static boolean joinRoom = false;
	static boolean leaveRoom = false;
	static boolean matchInit = false;
	static Activity activity = null;
	//newCoord no se usa en UIThread por lo que no es necesario
	//que se use en esta clase
	
	
	
	public static void init(Activity ac) {

		activity = ac;

		connectionLoop = new Thread(new Runnable() {
			@Override
			public void run() {
				// inicia una coneccion
				while (true) {
					if (startConnection) {
						Network.register(GameStates.client);
						NetworkListener rl = new NetworkListener();
						rl.init(GameStates.client);
						GameStates.client.addListener(rl);
						GameStates.client.start();
						try {
							// *cambiar timeout a 5000
							GameStates.client.connect(30000, Network.SERVER_IP,
									Network.PORT_TCP, Network.PORT_UDP);
							Log.d("debug", "se logro una coneccion");
							startConnection = false;
							isConnected = true;
							activity.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											activity,
											"Conectado con el servidor",
											Toast.LENGTH_SHORT).show();
								}
							});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							startConnection = false;
							isConnected = false;
							activity.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											activity,
											"No se a conectado con el servidor",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
						
					}

					// mensaje de registro
					if (sendRegister) {
						RequestRegisterUser rru = new RequestRegisterUser();
						rru.regInfo = GameStates.getJSONRegister(GameProperties
								.getFileIO());
						GameStates.client.sendTCP(rru);
						Log.d("debug", "se envio registro");
						sendRegister = false;
					}

					// mensaje de login
					if (sendLogin) {
						RequestLogin rl = new RequestLogin();
						rl.loginInfo = GameStates.getJSONLogin(GameProperties
								.getFileIO());
						GameStates.client.sendTCP(rl);
						sendLogin = false;
					}
					//crea partida
					if (createMatch) {
						RequestCreateRoom rcr = new RequestCreateRoom();
						rcr.userNameRoom = GameStates.username;
						GameStates.client.sendTCP(rcr);
						createMatch = false;
					}
					//lista partida
					if (listMatch) {
						RequestListRoom rcr = new RequestListRoom();
						rcr.list = new JSONObject();
						GameStates.client.sendTCP(rcr);
						listMatch = false;
					}
					//cierra partida
					if(closeRoom){
						RequestCloseRoom rcr = new RequestCloseRoom();
						Log.d("cierra sala",GameStates.username);
						rcr.userNameRoom = GameStates.username;
						GameStates.client.sendTCP(rcr);
						closeRoom = false;
					}
					//cambio de tiempo
					if(changeTime){
						RequestChangeTime rct = new RequestChangeTime();
						rct.timeMatch = ""+GameStates.timeMatch;
						rct.nameRoom = GameStates.username;
						GameStates.client.sendTCP(rct);
						changeTime = false;
					}
					//actualiza lista de equipo
					if(roomUpdate){
						RequestRoomUpdate rru = new RequestRoomUpdate();
						rru.nameRoom = GameStates.currMatch;
						rru.roomInfo = GameStates.getRoomInfo();
						GameStates.client.sendTCP(rru);
						roomUpdate = false;
					}
					//unirse a la sala
					if(joinRoom){
						RequestJoinRoom rjr = new RequestJoinRoom(); 
						rjr.nameRoom = GameStates.currMatch;
						rjr.username = GameStates.username;
						GameStates.client.sendTCP(rjr);
						joinRoom = false;
					}
					//Dejar la sala
					if(leaveRoom){
						RequestLeaveRoom rlr = new RequestLeaveRoom();
						rlr.nameRoom = GameStates.currMatch;
						rlr.namePlayer = GameStates.username;
						GameStates.client.sendTCP(rlr);
						leaveRoom = false;
					}
					if(matchInit){
						RequestMatchInit rmi = new RequestMatchInit();
						// no se envian los datos de la partida por que ya estan en el servidor
						rmi.nameRoom = GameStates.username;
						rmi.origin = GameProperties.getMatchOrigin();
						rmi.map = GameStates.getJSONMap();
						GameStates.client.sendTCP(rmi);
						matchInit = false;
					}
					// esta conectado
					if (isConnected) {
						isConnected = GameStates.client.isConnected();
						if (!isConnected) {
							activity.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											activity,
											"Se a perdido Coneccion con el servidor",
											Toast.LENGTH_LONG).show();
								}
							});
						}
					}
				}
			}
		});
		connectionLoop.start();
	}

	public static void setActivity(Activity ac) {
		activity = ac;
	}

	public static void connect() {
		startConnection = true;
	}

	public static void sendRegister() {
		sendRegister = true;
	}

	public static void sendLogin() {
		sendLogin = true;
	}

	public static void createMatch() {
		createMatch = true;
	}

	public static void listMatch() {
		listMatch = true;
	}
	public static void closeRoom() {
		closeRoom = true;
	}
	public static void changeTime() {
		changeTime = true;
	}

	public static void roomUpdate() {
		roomUpdate = true;
	}

	public static void joinRoom() {
		joinRoom = true;
	}

	public static void leaveRoom() {
		leaveRoom = true;
	}

	public static void matchInit() {
		matchInit = true;
	}
	
}
