/**
 * 
 */
package cl.geoconflict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.io.JSONStringer;

import com.badlogic.androidgames.framework.FileIO;

/**
 * @author Fernando Valencia F.
 * 
 */
public class Settings {
	public static boolean soundEnabled = true;

	public static void load(FileIO files) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile(".geoconflict")));
			soundEnabled = Boolean.parseBoolean(in.readLine());
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
	}

	public static void save(FileIO files) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(".geoconflict")));
			out.write(Boolean.toString(soundEnabled));
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

	public static void writeSaltSettings(String input) {

		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter("salt.txt"));
			out.write(input);
			out.close();
		} catch (IOException e) {
			System.out.println("There was a problem:" + e);
		}

	}

	public static String ReadSaltSettings() {

		BufferedReader in;
		String salt;
		try {
			in = new BufferedReader(new FileReader("salt.txt"));
			salt = in.readLine();
			in.close();
			return salt;
		} catch (IOException e) {
			System.out.println("There was a problem:" + e);
		}
		return null;
	}

}
