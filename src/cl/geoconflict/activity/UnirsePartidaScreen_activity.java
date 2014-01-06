package cl.geoconflict.activity;

import java.util.ArrayList;

import cl.geoconflict.ConnectionManager;
import cl.geoconflict.GameStates;
import cl.geoconflict.GeoConflictGame;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class UnirsePartidaScreen_activity extends Activity {

	ProgressDialog progressDialog;
	Handler LeaveRoomHandler;
	boolean inActivity;

	Handler changeTeamHandler;
	Handler changeTimeHandler;
	Handler initMatchHandler;

	AnimationDrawable frameAnimation;
	ArrayList<ImageView> teamList;
	ArrayList<TextView> teamListTx;
	ImageView clock;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.unirsepartidascreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);
		progressDialog = new ProgressDialog(this);
		LeaveRoomHandler = new Handler();
		inActivity = true;
		changeTeamHandler = new Handler();
		changeTimeHandler = new Handler();
		initMatchHandler = new Handler();

		clock = (ImageView) findViewById(R.id.clock);
		teamList = new ArrayList<ImageView>();
		teamListTx = new ArrayList<TextView>();
		
		teamList.add((ImageView) findViewById(R.id.small_layer_black));
		teamList.add((ImageView) findViewById(R.id.small_layer_black4));
		teamList.add((ImageView) findViewById(R.id.small_layer_black1));
		teamList.add((ImageView) findViewById(R.id.small_layer_black2));
		teamListTx.add((TextView) findViewById(R.id.tx_one));
		teamListTx.add((TextView) findViewById(R.id.tx_two));
		teamListTx.add((TextView) findViewById(R.id.tx_three));
		teamListTx.add((TextView) findViewById(R.id.tx_four));
		teamListTx.get(0).setText(GameStates.username);
		//desactiva elementos View
		cleanTeamList();
		
		// animacion del la imagen de espera
		final ImageView img = (ImageView) findViewById(R.id.iv_waitInit);
		img.setBackgroundResource(R.anim.spin_animation);
		
		img.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) img
							.getBackground();
				frameAnimation.start();
			}
		});

		changeTeamWaitTask();
		changeTimeWaitTask();
		initMatchWaitTask();
	}

	public void onClick_back(View v) {
		if (GameStates.client.isConnected()) {
			ConnectionManager.leaveRoom();
			LeaveRoomWaitTask();
		}else{
			Intent i = new Intent(UnirsePartidaScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (GameStates.client.isConnected()) {
			ConnectionManager.leaveRoom();
			LeaveRoomWaitTask();
		}else{
			Intent i = new Intent(UnirsePartidaScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}
	}

	public void changeTeamWaitTask() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.newUpdateRoom) {
						GameStates.newUpdateRoom = false;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				changeTeamHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIChangeTeam();
					}
				});

			}
		};
		thread.start();
	}

	public void changeTimeWaitTask() {
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
				changeTimeHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIChangeTime();
					}
				});

			}
		};
		thread.start();
	}

	public void initMatchWaitTask() {
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
				initMatchHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIInitMatch();
					}
				});

			}
		};
		thread.start();
	}

	public void LeaveRoomWaitTask() {
		// display the progressbar on the screen
		progressDialog.setMessage("Dejando la sala...");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.closeRoom) {
						GameStates.closeRoom = false;
						GameStates.roomJoined = false;
						GameStates.listReceived = false;
						GameStates.array.clear();
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				LeaveRoomHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUILeaveRoom();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};
		thread.start();
	}

	void updateUILeaveRoom() {
		inActivity = false;
		Intent i = new Intent(UnirsePartidaScreen_activity.this,
				MenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	private void updateUIChangeTeam() {
		Log.d("debug","updateUIChangeTeam");
		//limpia la lista
		cleanTeamList();
		
		if (GameStates.team.equals("red")) {
			teamList.get(0).setImageResource(R.drawable.medium_layer_red);
			// muestra miembros del equipo
			int j = 0;
			for (int i = 0; i < GameStates.teamRed.size(); i++) { 
				if (!GameStates.username.equals(GameStates.teamRed.get(i))) {
					teamList.get(j).setImageResource(R.drawable.small_layer_red);
					teamList.get(j).setVisibility(View.VISIBLE);
					teamListTx.get(j).setText(GameStates.teamRed.get(i));
					teamListTx.get(j).setVisibility(View.VISIBLE);
					j++;
				}
			}
		}else if (GameStates.team.equals("black")) {
			teamList.get(0).setImageResource(R.drawable.medium_layer_black);
			int j = 0;
			// muestra miembros del equipo
			for (int i = 0; i < GameStates.teamBlack.size(); i++) {
				if (!GameStates.username.equals(GameStates.teamBlack.get(i))) {
					teamList.get(j).setImageResource(R.drawable.small_layer_black);
					teamList.get(j).setVisibility(View.VISIBLE);
					teamListTx.get(j).setText(GameStates.teamBlack.get(i));
					teamListTx.get(j).setVisibility(View.VISIBLE);
					j++;
				}
			}
		}
		
		//Cambia tambien imageView de tiempo
		updateUIChangeTime();
		
		changeTeamWaitTask();
	}

	public void cleanTeamList(){
		//como son siempre del mismo tamaño  se usa un solo for
		for(int i = 1; i < teamList.size();i++){
			teamList.get(i).setVisibility(View.INVISIBLE);
			teamListTx.get(i).setVisibility(View.INVISIBLE);
		}
	}
	
	
	
	private void updateUIChangeTime() {
		Log.d("debug","updateUIChangeTime "+ GameStates.timeMatch + ", " + GameStates.team );
		if (GameStates.timeMatch == 10 && GameStates.team.equals("red"))
			clock.setImageResource(R.drawable.tenmin_red);
		if (GameStates.timeMatch == 15 && GameStates.team.equals("red"))
			clock.setImageResource(R.drawable.fifteemmin_red);
		if (GameStates.timeMatch == 20 && GameStates.team.equals("red"))
			clock.setImageResource(R.drawable.twentymin_red);
		if (GameStates.timeMatch == 10 && GameStates.team.equals("black"))
			clock.setImageResource(R.drawable.tenmin_black);
		if (GameStates.timeMatch == 15 && GameStates.team.equals("black"))
			clock.setImageResource(R.drawable.fifteenmin_black);
		if (GameStates.timeMatch == 20 && GameStates.team.equals("black"))
			clock.setImageResource(R.drawable.twentymin_black);
		
		changeTimeWaitTask();
	}

	private void updateUIInitMatch() {
		Intent i = new Intent(UnirsePartidaScreen_activity.this,
				GeoConflictGame.class);
		startActivity(i);
		finish();
	}
}
