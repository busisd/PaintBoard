package com.example.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

public class PaintView extends View {
    private Bitmap bitmap;
    private Canvas bitmapEditor;
    private Rect size;
    private int penRadius = 50;
    private ArrayList<Bitmap> undoStates;
    private ArrayList<Bitmap> redoStates;
    private Paint p;
    private int[] prevPos = new int[] {0,0};

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        undoStates = new ArrayList<>();
        redoStates = new ArrayList<>();

        p = new Paint();
        p.setStrokeWidth(50);
        p.setColor(Color.BLUE);
        p.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int newX, int newY, int oldX, int oldY){
        size = new Rect(0,0,newX,newY);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size.width(), size.height(), Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
            bitmapEditor = new Canvas(bitmap);
        }
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);
        c.drawBitmap(bitmap, null, size, null);
    }

    public void drawLines(LinkedList<int[]> positions){
        if (!positions.isEmpty()) {
            bitmapEditor.drawLine(prevPos[0],prevPos[1], positions.get(0)[0], positions.get(0)[1], p);
            for (int i=0; i<positions.size()-1;i++) {
                bitmapEditor.drawLine(positions.get(i)[0],positions.get(i)[1], positions.get(i+1)[0], positions.get(i+1)[1], p);
            }
            prevPos = positions.getLast();
        }
    }

    public void fingerDown(int[] position){
        prevPos = position;
        saveState();
    }

    private void saveState(){
        redoStates.clear();
        undoStates.add(0, bitmap.copy(bitmap.getConfig(), true));
        if (undoStates.size() > 5){
            undoStates.remove(undoStates.size()-1);
        }
    }

    public void reverseState(){
        if (!undoStates.isEmpty()) {
            Bitmap newMap = undoStates.remove(0);
            redoStates.add(0,bitmap.copy(bitmap.getConfig(), true));
            bitmap = newMap.copy(newMap.getConfig(), true);
            bitmapEditor = new Canvas(bitmap);
        }
    }

    public void forwardState(){
        if (!redoStates.isEmpty()) {
            Bitmap newMap = redoStates.remove(0);
            undoStates.add(0,bitmap.copy(bitmap.getConfig(),true));
            bitmap = newMap.copy(newMap.getConfig(), true);
            bitmapEditor = new Canvas(bitmap);
        }
    }

    public void setPenSize(int newSize){
        penRadius = newSize;
        p.setStrokeWidth(penRadius);
    }

    public void setColor(int newColor){
        p.setColor(newColor);
    }
}
