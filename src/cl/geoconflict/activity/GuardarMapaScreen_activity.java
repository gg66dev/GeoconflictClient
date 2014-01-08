package cl.geoconflict.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


public class GuardarMapaScreen_activity extends Activity {

	EditText nombreMapa;
	String externalStoragePath;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.guardarmapascreen);

		nombreMapa = (EditText) findViewById(R.id.etx_nombremapa);
		
		externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
	}	
	
	
	public void onClick_back(View v) {
		//guarda mapa denuevo con el nombre escogido
		RenameTempFile(nombreMapa.getText().toString());
		
		Intent i = new Intent(GuardarMapaScreen_activity.this,
				StartScreen_activity.class);
		startActivity(i);
		finish();
	}


	private void RenameTempFile(String fileName) {
		BufferedReader in = null;
		BufferedWriter out = null;
		String line;
		try {
			//lee archivo temporal
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(externalStoragePath + ".Temp")));
			//crea directorio
			File geoConflictDirectory = new File(externalStoragePath +"/GeoConflictMaps/");
			geoConflictDirectory.mkdirs();
			//Crea archivo de escritura
			File outputfile = new File(geoConflictDirectory,fileName);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputfile)));
			//traspasa contenido
			while ((line = in.readLine()) != null) {
				out.write(line);
				out.write("\n");
			}
			//borra temporal
			File file = new File(externalStoragePath + ".Temp");
			file.delete();
			
		} catch (IOException e) {
			// :( It's ok we have defaults
		} catch (NumberFormatException e) {
			// :/ It's ok, defaults save our day
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}


	public void onBackPressed() {
		Intent i = new Intent(GuardarMapaScreen_activity.this,
				StartScreen_activity.class);
		startActivity(i);
		finish();
	}
	
	

}
