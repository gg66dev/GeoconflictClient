package cl.geoconflict;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.badlogic.androidgames.framework.FileIO;
import com.esotericsoftware.kryonet.Client;

import android.util.Log;
import cl.geoconflict.utils.HashUtil;

/*
 * Esta clase funciona como intemediario entre los listeners y 
 * las screen que tiene la logica del juego * 
 * */
public class GameStates {

	// cliente
	static public Client client;

	// se asegura que metodo init de clases estaticas se llamen solo una vez
	static public boolean staticClassStarted = false;

	// variable que se confirma en la respuesta al servidor
	// en el networklistener
	static public boolean logged = false;
	static public boolean error = false;
	static public boolean registered = false;
	static public boolean roomAcepted = false;
	static public boolean roomJoined = false;
	static public boolean listReceived = false;
	static public boolean newUpdateRoom = false;
	static public boolean timeChange = false;
	static public boolean closeRoom = false;
	static public boolean initMatch = false;
	static public boolean hasTeam = false;
	public static boolean newPosition = false;

	static public boolean loading;

	static public String username;
	static public String passwd;
	static public String mail;

	// listar sala -unirse
	static public ArrayList<String> array = new ArrayList<String>(); // listas
																		// de
																		// salas
																		// disponibles
	static public ArrayList<String> teamRed = new ArrayList<String>();
	static public ArrayList<String> teamBlack = new ArrayList<String>();
	static public Hashtable<String, Double[]> positions = new Hashtable<String, Double[]>();
	static public String team;
	static public String currMatch;
	
	
	// crear partida
	static public int timeMatch;

	/**
	 * @return the positions
	 */
	public static Hashtable<String, Double[]> getPositions() {
		return positions;
	}

	static public void init() {
		client = new Client();
	}

	// retorna objeto Json que se enviara al servidor para login
	static public JSONObject getJSONLogin(FileIO files) {
		JSONObject obj = new JSONObject();
		// se obtiene salt de .geoconflcit
		String s = Settings.ReadSaltSettings(files);

		if (s != null) {
			ByteSource salt = ByteSource.Util.bytes(Hex.decode(s));
			String hashPass = new Sha1Hash(passwd, salt, 1024).toHex();
			try {
				obj.put("username", username);
				obj.put("passwd", hashPass); // envio passcodificado
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e("Error", "salt retorna null");
		}
		return obj;
	}

	// retorna objeto Json que se enviara al servidor para registrar
	static public JSONObject getJSONRegister(FileIO files) {
		JSONObject obj = new JSONObject();
		// encriptado de password
		ByteSource salt = HashUtil.getSalt();
		String hashPass = new Sha1Hash(passwd, salt, 1024).toHex();
		String s = salt.toHex();

		// guardar s en .geoconflict
		Settings.writeSaltSettings(files, s);

		try {
			obj.put("username", username);
			obj.put("passwd", hashPass); // envio passcodificado
			obj.put("mail", mail);
			obj.put("salt", s);
			Log.d("debug", obj.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	// lista de salas el arreglo json lo guarda como arraylist
	static public void setListRoom(JSONObject obj) {
		JSONArray list;
		Log.d("salas entregadas", obj.toString());
		// lista salas ya obtenidas
		array.clear();
		try {
			list = (JSONArray) obj.get("roomList");
			for (int i = 0; i < list.length(); i++) {
				array.add((String) list.get(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// info que envia el tiempo y los miembros de ambos equipos
	static public JSONObject getRoomInfo() {
		JSONObject obj = new JSONObject();
		JSONArray red = new JSONArray();
		JSONArray black = new JSONArray();
		red.put(teamRed);
		black.put(teamBlack);
		try {
			obj.put("time", timeMatch);
			obj.put("teamRed", teamRed);
			obj.put("teamBlack", teamBlack);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	// funcion usada por los usuarios normales para actualizar
	// las lista de equipo
	static public void setRoomUpdate(JSONObject obj) {
		JSONArray one = new JSONArray();
		JSONArray two = new JSONArray();
		// como actualizacion tiene nuevas listas se vacian las anteriores
		teamRed.clear();
		teamBlack.clear();

		try {
			one = obj.getJSONArray("teamRed");
			two = obj.getJSONArray("teamBlack");
			timeMatch = obj.getInt("time");
			Log.d("debug", "red" + one.toString());
			Log.d("debug", "black" + two.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < one.length(); i++) {
			try {
				teamRed.add((String) one.get(i));
				if (one.get(i).equals(username)) {
					team = "red";
					hasTeam = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < two.length(); i++) {
			try {
				teamBlack.add((String) two.get(i));
				if (two.get(i).equals(username)) {
					team = "black";
					hasTeam = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("debug", "se termino de actualizar lista de equipos");
	}

	
	/**
	 * Realiza la inicializaci&oacute;n de la partida
	 * @param numPlayers Cantidad de players del equipo de este cliente
	 * @param positions posiciones de los compa&ntilde;eros de equipo
	 */
	static public void initMatch(int numPlayers, JSONArray posJsonArray ){
		initMatch = true;
		// positions es un JSONArray de JSONObjects con llaves "x" e "y"
		// con la posicion de los jugadores
		positions = new Hashtable<String, Double[]>();
		for(int i=0; i<posJsonArray.length(); i++ ){
			try {
				JSONObject o = (JSONObject) posJsonArray.get(i);
				positions.put( o.get("nombre").toString(), 
						new Double[] {(Double) o.get("x"), (Double) o.get("y")} );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// cambia A rojo a negro y B negro a rojo
	public static void changeTeamRedBlack(String playerA, String playerB) {
		int positionRed = teamRed.indexOf(playerA);
		int positionBlack = teamBlack.indexOf(playerB);
		teamRed.set(positionRed, playerB);
		teamBlack.set(positionBlack, playerA);
	}

}
