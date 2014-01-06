package cl.geoconflict.activity;

import cl.geoconflict.ConnectionManager;
import cl.geoconflict.GameStates;
import cl.geoconflict.gui.ListViewAdapter;
import cl.geoconflict.gui.ListViewItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PartidasDisponibleScreen_activity extends Activity {
	
	Handler joinRoomHandler;
	ProgressDialog progressDialog;
	boolean result = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.partidasdisponiblescreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);
		joinRoomHandler = new Handler();
		final ListView lista = (ListView) findViewById(R.id.listroom);
		
		/* Asigna ListView segun respuesta del servidor */
		//cuando hay salas
		if (GameStates.array.size() > 0) {
			//pasa arreglo de string a ListViewItem
			ListViewItem[] roomsArray = new ListViewItem[GameStates.array.size()];
			for(int i=0; i < GameStates.array.size(); i++ ){
				roomsArray[i] = new ListViewItem();
				roomsArray[i].title = GameStates.array.get(i);
				roomsArray[i].background = R.drawable.small_layer_red;
			}
			// guarda lista de salas y luego las asigna al adapter 
			ListViewAdapter adapter = new ListViewAdapter(this, 
	                R.layout.listview_item_row, roomsArray);
			 lista.setAdapter(adapter);
			 //crea el ClickListener
			 lista.setOnItemClickListener(new OnItemClickListener(){
				 public void onItemClick(AdapterView<?> adapter, View view,
						int position, long arg) {
					ListViewItem item = (ListViewItem) lista.getItemAtPosition(position);
					GameStates.currMatch = item.title;
					//envia request para unirse a sala
					ConnectionManager.joinRoom();
					JoinRoomWaitTask();
				}
				 
			 });
		}
		// no hay salas disponibles
		if (GameStates.array.size() == 0) {
			ListViewItem[] roomsArray = new ListViewItem[1];
			roomsArray[0] = new ListViewItem();
			roomsArray[0].title = "no hay salas disponibles";
			roomsArray[0].background = R.drawable.small_layer_red;
			ListViewAdapter adapter = new ListViewAdapter(this, 
	                R.layout.listview_item_row, roomsArray);
			 lista.setAdapter(adapter);
		}
	}
	
	public void onClick_back(View v) {
		if (GameStates.client.isConnected()) {
			Intent i = new Intent(PartidasDisponibleScreen_activity.this,
					MenuScreen_activity.class);
			startActivity(i);
			finish();
		}else{
			Intent i = new Intent(PartidasDisponibleScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}
			
		
	}

	// se une a la sala seleccionada
	private void JoinRoomWaitTask() {
		// display the progressbar on the screen
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Uniendose a la sala...");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.roomJoined) {
						result = true;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				joinRoomHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIJoinRoom();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};

		thread.start();
	}

	void updateUIJoinRoom() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (!result) {
			alt_bld.setMessage("Error -- no se pudo unir a la Partida");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok",null);
			alt_bld.show();
		} else {
			// pasa a la ventana de crear partida.
			Intent i = new Intent(PartidasDisponibleScreen_activity.this,
					UnirsePartidaScreen_activity.class);
			startActivity(i);
			finish();
		}
	}
	
}
