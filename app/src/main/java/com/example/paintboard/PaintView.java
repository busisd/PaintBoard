package com.example.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class PaintView extends View {
    private Bitmap bitmap;
    private Canvas bitmapEditor;
    private Rect size;
    private int penRadius = 50;
    private Paint p;

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
        p = new Paint(Color.BLACK);
        p.setStrokeWidth(0);
        p.setStyle(Paint.Style.FILL);
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

    public void paintAt(int centerX, int centerY){
        bitmapEditor.drawCircle(centerX, centerY, penRadius, p);
    }

    private boolean isPosValid(int x, int y){
        return (x < size.width() && y < size.height() && x >= 0 && y >= 0);
    }

    public void saveState(){

    }

    public void reverseState(){

    }

    public void forwardState(){

    }

    public void changePenSize(int change){
        penRadius += change;
        if (penRadius<=0) penRadius = 5;
    }

    public void setColor(int newColor){
        p.setColor(newColor);
    }
}
