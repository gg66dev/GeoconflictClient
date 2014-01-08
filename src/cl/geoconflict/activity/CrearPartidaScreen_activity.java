package cl.geoconflict.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cl.geoconflict.ConnectionManager;
import cl.geoconflict.GameStates;
import cl.geoconflict.GeoConflictGame;
import cl.geoconflict.gameplay.Collisionable;
import cl.geoconflict.gui.ListViewAdapter;
import cl.geoconflict.gui.ListViewItem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CrearPartidaScreen_activity extends Activity {

	ListView lista;
	ListViewItem[] usersArray;

	ProgressDialog progressDialog;
	boolean result = false;
	boolean inActivity = false; // si recive respuesta de nuevo usuario fuera de
								// la activitdad
	Handler newUserHandler;
	Handler changeTimeHandler;
	Handler closeRoomHandler;
	Handler initMatchHandler;

	Button bt_ten;
	Button bt_fifteen;
	Button bt_twenty;

	// variables para cambiar usuario de equipo
	String playerA;
	String playerB;
	View firstSelected = null;
	String teamFirstSelected;
	Drawable originalDrawable;

	String externalStoragePath;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inActivity = true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.crearpartidascreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);
		progressDialog = new ProgressDialog(this);
		newUserHandler = new Handler();
		changeTimeHandler = new Handler();
		closeRoomHandler = new Handler();
		initMatchHandler = new Handler();
		lista = (ListView) findViewById(R.id.listuser);
		bt_ten = (Button) findViewById(R.id.tenmin);
		bt_fifteen = (Button) findViewById(R.id.fifteenmin);
		bt_twenty = (Button) findViewById(R.id.twentymin);
		GameStates.timeMatch = 10;

		externalStoragePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;

		NewUserWaitTask();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(GameStates.mapLoaded){
			Log.d("se cargo el mapa ", GameStates.mapFile);
		}
	}

	// inicia partida
	public void onClick_empezar(View v) {
		if(GameStates.mapLoaded){
			loadMap(GameStates.mapFile);
		}
		ConnectionManager.matchInit();
		InitMatchWaitTask();
	}

	// hace lo mismo que la funcion de retroceso
	public void onClick_back(View v) {
		// close room
		if (GameStates.client.isConnected()) {
			ConnectionManager.closeRoom();
			CloseRoomWaitTask();
		} else {
			Intent i = new Intent(CrearPartidaScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}

	}

	// 10 min
	public void onClick_tenmin(View v) {
		// display the progressbar on the screen
		progressDialog.setMessage("Realizando cambio...");
		progressDialog.show();
		Log.d("debug", "se preciono 10");
		GameStates.timeMatch = 10;
		ConnectionManager.changeTime();
		ChangeTimeWaitTask();
	}

	// 15 min
	public void onClick_fifteenmin(View v) {
		// display the progressbar on the screen
		progressDialog.setMessage("Realizando cambio...");
		progressDialog.show();
		Log.d("debug", "se preciono 15");
		GameStates.timeMatch = 15;
		ConnectionManager.changeTime();
		ChangeTimeWaitTask();
	}

	// 20 min
	public void onClick_twentymin(View v) {
		// display the progressbar on the screen
		progressDialog.setMessage("Realizando cambio...");
		progressDialog.show();
		Log.d("debug", "se preciono 20");
		GameStates.timeMatch = 20;
		ConnectionManager.changeTime();
		ChangeTimeWaitTask();
	}

	// carga mapa
	public void onClick_sd_up(View v) {
			//loadMap("cuatro");
		Intent i = new Intent(CrearPartidaScreen_activity.this,
				CargarMapaScreen_activity.class);
		startActivity(i);
	}

	void loadMap(String fileName){
		BufferedReader in = null;
		String line;
		Log.d("debug","en loadMap");
		try {
			//lee archivo de la targeta
			File geoConflictDirectory = new File(externalStoragePath +"/GeoConflictMaps/"+fileName);
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(geoConflictDirectory)));
			//traspasa contenido al arrayList
			while ((line = in.readLine()) != null) {
				String[] coll = line.split("-");
				int x = Integer.parseInt(coll[0]);
				int y = Integer.parseInt(coll[1]);
				int w = Integer.parseInt(coll[2]);
				int h = Integer.parseInt(coll[3]);
				Log.d("debug", coll[0]+","+coll[1]+","+coll[2]+","+coll[3]);
				GameStates.collList.add(new Collisionable(x,y,w,h));
			}
			
		} catch (IOException e) {
			// :( It's ok we have defaults
			Toast.makeText(getApplicationContext(), 
                    "No hay Mapas en la memoria", Toast.LENGTH_SHORT).show();
			Log.d("debug","error al leer archivo");
		} catch (NumberFormatException e) {
			// :/ It's ok, defaults save our day
			Log.d("debug","otro error");
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	}
	

	// tarea que espera que ingresen nuevos usuarios
	private void NewUserWaitTask() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.newUpdateRoom && inActivity) {
						Log.d("debug", "cambio estado newUpdateRoom");
						GameStates.newUpdateRoom = false;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				newUserHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUINewUser();
					}
				});
			}
		};
		thread.start();
	}

	// tarea que espera inicio de partida
	public void InitMatchWaitTask() {
		// display the progressbar on the screen
		progressDialog.setMessage("Iniciando partida");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.initMatch) {
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				initMatchHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIMatchInit();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};
		thread.start();
	}

	// tarea que espera que se cierre la sala
	public void CloseRoomWaitTask() {
		// display the progressbar on the screen
		progressDialog.setMessage("Cerrando sala...");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.closeRoom) {
						GameStates.roomAcepted = false;
						GameStates.closeRoom = false;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				closeRoomHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUICloseRoom();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};
		thread.start();
	}

	// tarea que espera respuesta a cambio de tiempo
	public void ChangeTimeWaitTask() {
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.timeChange) {
						GameStates.timeChange = false;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				changeTimeHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIChangeTime();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};
		thread.start();
	}

	void updateUINewUser() {
		lista.setAdapter(null);
		lista.setOnItemClickListener(null);

		// se reasigna cada vez que recive nueva lista de usuarios
		usersArray = new ListViewItem[GameStates.teamRed.size()
				+ GameStates.teamBlack.size()];
		int i = 0;
		if (GameStates.teamRed.size() > 0) {
			for (String p : GameStates.teamRed) {
				usersArray[i] = new ListViewItem();
				usersArray[i].title = p;
				usersArray[i].team = "red";
				usersArray[i].background = R.drawable.small_layer_red;
				Log.d("debug", "agregado " + p);
				i++;
			}
		}
		if (GameStates.teamBlack.size() > 0) {
			for (String p : GameStates.teamBlack) {
				usersArray[i] = new ListViewItem();
				usersArray[i].title = p;
				usersArray[i].team = "black";
				usersArray[i].background = R.drawable.small_layer_black;
				Log.d("debug", "agregado " + p);
				i++;
			}
		}
		ListViewAdapter adapter = new ListViewAdapter(this,
				R.layout.listview_item_row, usersArray);
		lista.setAdapter(adapter);

		// logica de cambio de equipo
		lista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {

				// Log.d("debug","largo adaptador "+adapter.getCount());
				if (firstSelected == null) {
					// Log.d("debug","cantidad: " + adapter.getCount());
					// Log.d("debug","posicion: " + position);
					// Log.d("debug","valor a consultar: " + (adapter.getCount()
					// - position - 1));
					firstSelected = adapter.getChildAt(adapter.getCount()
							- position - 1);
					originalDrawable = view.getBackground();
					ListViewItem item = (ListViewItem) lista
							.getItemAtPosition(position);
					playerA = item.title;
					teamFirstSelected = item.team;
					if (teamFirstSelected.equals("red"))
						firstSelected
								.setBackgroundResource(R.drawable.hugh_layer_black);
					else
						firstSelected
								.setBackgroundResource(R.drawable.hugh_layer_red);
				} else {

					ListViewItem item = (ListViewItem) lista
							.getItemAtPosition(position);
					playerB = item.title;
					// revisa que no sean del mismo equipo
					if (!item.team.equals(teamFirstSelected)) {
						if (teamFirstSelected.equals("red"))
							GameStates.changeTeamRedBlack(playerA, playerB);
						else
							GameStates.changeTeamRedBlack(playerB, playerA);
						// envia actualizacion al servidor
						ConnectionManager.roomUpdate();
					}
					firstSelected.setBackgroundDrawable(originalDrawable);
					firstSelected = null;
				}
			}

		});

		// Se llama denuevo a esperar una nueva actualizacion
		NewUserWaitTask();
	}

	void updateUIMatchInit() {
		Intent i = new Intent(CrearPartidaScreen_activity.this,
				GeoConflictGame.class);
		startActivity(i);
		finish();
	}

	void updateUICloseRoom() {
		inActivity = false;
		Intent i = new Intent(CrearPartidaScreen_activity.this,
				MenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	void updateUIChangeTime() {
		if (GameStates.timeMatch == 10) {
			bt_ten.setBackgroundResource(R.drawable.tenmin_red);
			bt_fifteen.setBackgroundResource(R.drawable.fifteenmin_black);
			bt_twenty.setBackgroundResource(R.drawable.twentymin_black);
		} else if (GameStates.timeMatch == 15) {
			bt_ten.setBackgroundResource(R.drawable.tenmin_black);
			bt_fifteen.setBackgroundResource(R.drawable.fifteemmin_red);
			bt_twenty.setBackgroundResource(R.drawable.twentymin_black);
		} else if (GameStates.timeMatch == 20) {
			bt_ten.setBackgroundResource(R.drawable.tenmin_black);
			bt_fifteen.setBackgroundResource(R.drawable.fifteenmin_black);
			bt_twenty.setBackgroundResource(R.drawable.twentymin_red);
		}
	}

	// hace lo mismo que el boton back
	@Override
	public void onBackPressed() {
		if (GameStates.client.isConnected()) {
			ConnectionManager.closeRoom();
			CloseRoomWaitTask();
		} else {
			Intent i = new Intent(CrearPartidaScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}
	}

}
