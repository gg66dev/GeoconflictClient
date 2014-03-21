/**
 * 
 */
package cl.geoconflict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import cl.geoconflict.gameplay.Collisionable;

import com.badlogic.androidgames.framework.FileIO;

/**
 * @author Fernando Valencia F.
 * 
 */
public class Settings {
	public static boolean soundEnabled = true;
	public static String ipServer = "";
	public static String returnMsg = "";
	
	public static boolean load(FileIO files) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile("geoconflict.conf.txt")));
			JSONObject obj = new JSONObject(in.readLine());
			//carga configuracion
			soundEnabled = Boolean.parseBoolean((String) obj.get("sound"));
			ipServer = (String)obj.get("server");
		} catch (IOException e) {
			// :( It's ok we have defaults
			return false;
		} catch (NumberFormatException e) {
			// :/ It's ok, defaults save our day
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	//valida formato de la ip
	public static boolean validarIP() {
		if(ipServer.equals("***.***.***.***")){
			returnMsg = "-no se ha asignado Ip del servidor";
			return false;
		}
		String[] str = ipServer.split("\\.");
		if(str.length != 4){
			returnMsg = "-error ingreso IP";
			return false;
		}
		for(int i = 0; i < str.length;i++){
			if(!isInteger(str[i])){
				returnMsg = "-error ingreso IP";
				return false;
			}
		}
		return true;
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	
	public static boolean save(FileIO files) {
		BufferedWriter out = null;
		JSONObject obj = new JSONObject();
		try {
			obj.put("sound",Boolean.toString(soundEnabled));
			obj.put("server","***.***.***.***"); //talves despues poner un servidor por defecto
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile("geoconflict.conf.txt")));
			out.write(obj.toString());
			out.write("\n");
		} catch (IOException e) {
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
	
	
	public static void saveMap(FileIO files, String name, ArrayList<Collisionable> collList) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(name)));
			for(Collisionable x : collList){
				out.write(""+x.x+"-"+x.y+"-"+x.w+"-"+x.h);
				out.write("\n");
			}
		} catch (IOException e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}

	public static void writeSaltSettings(FileIO files, String input) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile("salt.txt")));
			out.write(input);
			out.write("\n");
		} catch (IOException e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}

	public static String ReadSaltSettings(FileIO files) {
		BufferedReader in = null;
		String salt;
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile("salt.txt")));
			salt = in.readLine();
			return salt;
		} catch (IOException e) {
			// :( It's ok we have defaults
		} catch (NumberFormatException e) {
			// :/ It's ok, defaults save our day
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}
