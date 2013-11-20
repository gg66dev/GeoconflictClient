package cl.geoconflict;

import java.io.IOException;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.badlogic.androidgames.framework.FileIO;

import android.util.Log;

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
	
	public String username;
	public String passwd;
	public String mail;
	
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
}
