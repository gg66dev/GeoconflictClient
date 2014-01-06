package cl.geoconflict;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidAudio;
import com.badlogic.androidgames.framework.impl.AndroidFileIO;
import com.badlogic.androidgames.framework.impl.AndroidInput;

import android.app.Activity;

public class GameProperties {

	static Audio audio;
	static Input input;
	static FileIO fileIO;
	static Screen screen;

	/*
	 * codigo de AndroidGame, para que estas propiedades inicien al comienzo del
	 * juego. El resto del framework inicia luego de crear partida
	 */
	static public void init(Activity activity) {

		fileIO = new AndroidFileIO(activity.getAssets());
		audio = new AndroidAudio(activity);
		input = new AndroidInput(activity);
	}

	static public FileIO getFileIO() {
		return fileIO;
	}

	static public Input getInput() {
		return input;
	}

	public static Audio getAudio() {
		return audio;
	}

	public static JSONObject getMatchOrigin() {
		JSONObject obj = new JSONObject();
		Double latitud = input.getLatitud();
		Double longitud = input.getLongitud();
		try {
			obj.put("latitud", latitud);
			obj.put("longitud", longitud);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;

	}

}
