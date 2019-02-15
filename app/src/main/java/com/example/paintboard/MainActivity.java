package com.example.paintboard;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    PaintView view;
    int statusBarHeight = 0;
    CountDownTimer repeat;
    LinkedList<int[]> positions = new LinkedList<int[]>();
    TextView fpsMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.paintView);
        fpsMeter = findViewById(R.id.FPSMeter);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId>0){
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        repeat = new CountDownTimer((long) 16.6666,2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                updateFPS();
                draw();
            }
        }.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        positions.push(new int[] {x,y-statusBarHeight});
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = event.getHistorySize() - 1; i >= 0; i--) {
                x =  Math.round(event.getHistoricalX(i));
                y =  Math.round(event.getHistoricalY(i));
                positions.push(new int[] {x,y-statusBarHeight});
            }
        }
        return true;
    }

    public void draw(){
        int[] cur;
        while(positions.size() > 0) {
            cur = positions.removeLast();
            view.paintAt(cur[0], cur[1]);
        }
        view.invalidate();
        repeat.start();
    }

    private long prevTime = 0;
    private void updateFPS(){
        long curTime = SystemClock.elapsedRealtime();
        long curFPS = 1000/(curTime-prevTime);
        prevTime = curTime;
        String FPSOut = "FPS: "+Long.toString(curFPS);
        fpsMeter.setText(FPSOut);
    }

    public void increasePenSize(View v){
        view.changePenSize(5);
    }

    public void decreasePenSize(View v){
        view.changePenSize(-5);
    }

    public void setRandomColor(View v){
        int a = (int) (Math.random()*256);
        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        view.setColor(Color.argb(a,r,g,b));
    }
}
