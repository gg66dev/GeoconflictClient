package cl.geoconflict.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.geoconflict.GameStates;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CargarMapaScreen_activity extends Activity {

	String externalStoragePath;
	Spinner spinner;
	TextView msgScreen;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cargadormapascreen);

		externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;

		spinner = (Spinner) findViewById(R.id.spinner1);
		msgScreen = (TextView) findViewById(R.id.msgScreen);
		List<String> list = new ArrayList<String>();
		
		
		File f = new File(externalStoragePath + "/GeoConflictMaps");
		File file[] = f.listFiles();
		if(file!=null){
			for (int i = 0; i < file.length; i++) {
				list.add(file[i].getName());
				Log.d("debug",file[i].getName());
			}
		}
		else{
			spinner.setVisibility(View.INVISIBLE);
			GameStates.mapFile  = "no hay mapas en la memoria SD";
			msgScreen.setText("No Hay Mapas");
		}
		/*ocultar spinner cuando no haya archivos*/
		

		// obtienen un arreglo de String para mostrarlo en spinners
		String[] simpleNameFileArray = new String[list.size()];
		list.toArray(simpleNameFileArray);

		ArrayAdapter<String> adapterSector;
		adapterSector = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, simpleNameFileArray);
		spinner.setAdapter(adapterSector);
		
		
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				GameStates.mapFile =  parentView.getItemAtPosition(position).toString();
				GameStates.mapLoaded = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub
				GameStates.mapFile = "no se ha seleccionado ningun mapa";
				GameStates.mapLoaded = false;
			}

		});

	}
	
	// hace lo mismo que la funcion de retroceso
		public void onClick_back(View v) {
			if(GameStates.mapLoaded)
				Toast.makeText(getApplicationContext(), 
						"se cargo mapa, " + GameStates.mapFile, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), 
						 GameStates.mapFile, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		@Override
		public void onBackPressed() {
			finish();
		}

}
