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

import cl.geoconflict.gameplay.Collisionable;

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
