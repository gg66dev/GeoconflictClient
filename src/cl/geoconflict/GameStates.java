package cl.geoconflict;


import java.util.ArrayList;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.badlogic.androidgames.framework.FileIO;

import android.util.Log;

import cl.geoconflict.gps.PositionGPS;
import cl.geoconflict.utils.HashUtil;

/*
 * Esta clase funciona como intemediario entre los listeners y 
 * las screen que tiene la logica del juego * 
 * */
public class GameStates {

	//variable que se confirma en la respuesta al servidor
	//en el networklistener
	public boolean logged = false;
	public boolean error = false;
	public boolean registered = false;
	public boolean roomAcepted = false;
	public boolean roomJoined = false;
	public boolean listReceived = false;
	public boolean newUpdateRoom = false;
	public boolean timeChange = false;
	public boolean closeRoom = false;
	public boolean initMatch = false;
	public boolean hasTeam = false;
	
	public boolean loading;
	
	public String username;
	public String passwd;
	public String mail;
	
	//listar sala -unirse
	public ArrayList<String> array = new ArrayList<String>(); //listas de salas disponibles
	public ArrayList<String> teamRed = new ArrayList<String>();
	public ArrayList<String> teamBlack = new ArrayList<String>();
	public String team;
	public String currMatch;
	
	//crear partida
	public int timeMatch;
	
	//GPS
	public PositionGPS gps;
	
	
	//retorna objeto Json que se enviara al servidor para login
	public JSONObject getJSONLogin(FileIO files){
		JSONObject obj = new JSONObject();
		//se obtiene salt de .geoconflcit
		String s = Settings.ReadSaltSettings(files);
		
		if(s != null){
			ByteSource salt = ByteSource.Util.bytes(Hex.decode(s));
			String hashPass = new Sha1Hash(passwd, salt, 1024).toHex();
			try {
				obj.put("username",username);
				obj.put("passwd",hashPass);	//envio passcodificado
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			Log.e("Error","salt retorna null");
		}
		return obj;
	}
	
	//retorna objeto Json que se enviara al servidor para registrar
	public JSONObject getJSONRegister(FileIO files){
		JSONObject obj = new JSONObject();
		//encriptado de password
		ByteSource salt = HashUtil.getSalt();
		String hashPass = new Sha1Hash(passwd, salt, 1024).toHex();
		String s = salt.toHex();
				
		//guardar s en .geoconflict
		Settings.writeSaltSettings(files,s);		

		try {
			obj.put("username",username);
			obj.put("passwd",hashPass);	//envio passcodificado
			obj.put("mail",mail);	
			obj.put("salt", s);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	//lista de salas el arreglo json lo guarda como arraylist
	public void setListRoom(JSONObject obj){
		JSONArray list;
		Log.d("salas entregadas",obj.toString());
		try {
			list = (JSONArray) obj.get("roomList");
			for (int i = 0; i < list.length(); i++) {
				array.add((String)list.get(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	//info que envia el tiempo y los miembros de ambos equipos
	public JSONObject getRoomInfo() {
		JSONObject obj = new JSONObject();
		JSONArray red = new JSONArray();
		JSONArray black = new JSONArray();
		red.put(teamRed);
		black.put(teamBlack);
		try {
			obj.put("time",timeMatch);
			obj.put("teamRed",teamRed);
			obj.put("teamBlack",teamBlack);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	//funcion usada por los usuarios normales para actualizar
	//las lista de equipo
	public void setRoomUpdate(JSONObject obj){
		JSONArray one = new JSONArray();
		JSONArray two = new JSONArray();
		//como actualizacion tiene nuevas listas se vacian las anteriores
		teamRed.clear();
		teamBlack.clear();		
		
		try {
			one = obj.getJSONArray("teamRed");
			two = obj.getJSONArray("teamBlack");
			timeMatch = obj.getInt("time");
			Log.d("debug","red" + one.toString());
			Log.d("debug","black" + two.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < one.length();i++){
			try {
				teamRed.add((String) one.get(i));
				if(one.get(i).equals(username)){
					team = "red";
					hasTeam = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < two.length();i++){
			try {
				teamBlack.add((String) two.get(i));
				if(two.get(i).equals(username)){
					team = "black";
					hasTeam = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public JSONObject getOrigin() {
		JSONObject obj = new JSONObject();
		Double longitud = gps.getLatitud();
		Double latitud = gps.getLongitud();
		try {
			obj.put("latitud",latitud);
			obj.put("longitud",longitud);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}
