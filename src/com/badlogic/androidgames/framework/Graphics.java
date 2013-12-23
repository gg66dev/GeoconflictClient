package com.badlogic.androidgames.framework;

import android.graphics.Paint.Style;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);
    
    public void drawRect(int x, int y, int width, int height, int color);
    
    public void drawRect(int x, int y, int width, int height, int color, Style style);

    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawPixmap(Pixmap pixmap, int x, int y);

    public void drawPixmap(Pixmap pixmap, int x, int y,float rotateAngle);
    
    public void drawText(String Text,int x, int y, int color,int size);
    
    //rota  texto respecto al centro de la pantalla
    public void drawTextRotate(String text,int x, int y, int color, int size,int rotateAngle);
    
    
    public int getWidth();

    public int getHeight();
}
