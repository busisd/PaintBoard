package com.example.paintboard;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    PaintView view;
    int statusBarHeight = 0;
    CountDownTimer repeat;
    LinkedList<int[]> positions = new LinkedList<int[]>();
    TextView fpsMeter;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.paintView);
        fpsMeter = findViewById(R.id.FPSMeter);
        seekBar = findViewById(R.id.seekBar);

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
        view.setPenSze(seekBar.getProgress()+5);
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
//        int a = (int) (Math.random()*256);
        int a = 255;
        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        view.setColor(Color.argb(a,r,g,b));
    }
}


//Todo: Interpolation between circles (draw lines?)
//Todo: Use a SeekBarListener
//Todo: Come up with a solution for colors (using one or more SeekBars?)
//Todo: Save states! (Undo/redo!)
//Todo: Make menu/buttons look nice!
//Todo: Graphic in menu to display pen size when changing it!
//Todo: Make the SeekBar more visible when at the left!