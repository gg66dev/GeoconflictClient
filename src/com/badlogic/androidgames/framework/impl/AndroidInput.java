package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.badlogic.androidgames.framework.Input;

public class AndroidInput implements Input {    
    AccelerometerHandler accelHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;
    LocationHandler locationHandler;
    
    //agregado para mostrar soft keyboard
    InputMethodManager inputManager;
    View view;
    

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);
        locationHandler = new LocationHandler(context); //GPS
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.view = view;
        
        if(Integer.parseInt(VERSION.SDK) < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }

    public void showkeyboard(){
    	  	inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    
    public void hidekeyboard(){
    		inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
    
    
    @Override
    public double getLatitud(){
    	return locationHandler.getLatitud();
	}
    
    @Override
    public double getLongitud(){
		return locationHandler.getLongitud();
	}
    
    @Override
    public boolean isLocationChanged(){
		return locationHandler.isLocationChanged();
	}
	
    @Override
    public void notLocationChanged(){
		locationHandler.notLocationChanged();
	}
}
