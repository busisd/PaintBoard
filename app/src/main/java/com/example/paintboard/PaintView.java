package com.example.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class PaintView extends View {
    private int[] colorArray;
    private Bitmap bitmap;
    private Rect size;
    private Rect arraySize;
    private int curColor = Color.BLACK;
    private int penRadius = 50;


    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int newX, int newY, int oldX, int oldY){
        size = new Rect(0,0,newX,newY);
        arraySize = new Rect(0,0,newX/4,newY/4);
        if (bitmap == null) {
            colorArray = new int[newX/4*newY/4];
            Arrays.fill(colorArray, Color.WHITE);
            bitmap = Bitmap.createBitmap(arraySize.width(), arraySize.height(), Bitmap.Config.ARGB_8888);
        }
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);
        bitmap.setPixels(colorArray, 0, arraySize.width(), 0, 0, arraySize.width(), arraySize.height());
        c.drawBitmap(bitmap, null, size, null);
    }

    public void paintAt(int centerX, int centerY){
        for (int x = -penRadius; x<=penRadius; x++){
            int yStart = -(int)Math.sqrt(Math.pow(penRadius,2)-Math.pow(x,2));
            int yEnd = (int)Math.sqrt(Math.pow(penRadius,2)-Math.pow(x,2));
            for (int y = yStart; y <= yEnd; y++) {
                if (isPosValid(x+centerX, y+centerY)) colorArray[arrayPos((x+centerX)/4,(y+centerY)/4)] = curColor;
            }
        }
    }

    private boolean isPosValid(int x, int y){
        return (x < size.width() && y < size.height() && x >= 0 && y >= 0);
    }

    private int arrayPos(int x, int y){
    return y*arraySize.width()+x;
    }

    public int[] getColorArray(){
        return colorArray;
    }

    public void saveState(){

    }

    public void reverseState(){

    }

    public void forwardState(){

    }
}
