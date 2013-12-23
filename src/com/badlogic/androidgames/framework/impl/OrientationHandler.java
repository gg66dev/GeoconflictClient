package com.badlogic.androidgames.framework.impl;

import java.util.List;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationHandler implements SensorEventListener {

	
	private static SensorManager mySensorManager;
	private boolean sensorrunning;
	private float direction = 0;
	
	
	public OrientationHandler(Context context){
		mySensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        
        if(mySensors.size() > 0){
        	mySensorManager.registerListener(this, mySensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        	sensorrunning = true;
        }
        else{
        	sensorrunning = false;
        }
	}
	
//	public void destroy(){
//		if(sersorrunning){
//			mySensorManager.unregisterListener(this);	
//		}
//	}
	
	public float getDirection(){
		return direction;
	}
	
	public boolean isSensorRunning(){
		return sensorrunning;
	}
		
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		direction = (float)event.values[0];
	}

}
