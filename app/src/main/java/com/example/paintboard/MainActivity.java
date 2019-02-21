package com.example.paintboard;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    PaintView view;
    int statusBarHeight = 0;
    CountDownTimer repeat;
    LinkedList<int[]> positions = new LinkedList<>();
    TextView fpsMeter;
    SeekBar seekBar;
    LinearLayout optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.paintView);
        fpsMeter = findViewById(R.id.FPSMeter);
        seekBar = findViewById(R.id.seekBar);
        optionsMenu = findViewById(R.id.optionsMenu);

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
        view.setPenSize(seekBar.getProgress()*2+5);
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int x;
            int y;
            for (int i = 0; i < event.getHistorySize(); i++) {
                x =  Math.round(event.getHistoricalX(i));
                y =  Math.round(event.getHistoricalY(i));
                positions.add(new int[] {x,y-statusBarHeight});
            }
            x = Math.round(event.getX());
            y = Math.round(event.getY());
            positions.add(new int[] {x,y-statusBarHeight});

        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int[] pos = {(int)event.getX(), (int)event.getY()-statusBarHeight};
            view.fingerDown(pos);
        }
        return true;
    }

    public void draw(){
        view.drawLines(positions);
        positions.clear();
        view.invalidate();
        repeat.start();
    }

    private long prevTime = 0;
    private void updateFPS(){
        long curTime = SystemClock.elapsedRealtime();
        long curFPS = 1000/(curTime-prevTime);
        prevTime = curTime;
        String FPSOut = "        FPS: "+Long.toString(curFPS);
        fpsMeter.setText(FPSOut);
    }

    public void setRandomColor(View v){
//        int a = (int) (Math.random()*256);
        int a = 255;
        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        view.setColor(Color.argb(a,r,g,b));
    }

    public void toggleOptions(View v){
        int curVis = optionsMenu.getVisibility();
        if (curVis == View.VISIBLE) optionsMenu.setVisibility(View.GONE);
        else optionsMenu.setVisibility(View.VISIBLE);
    }

    public void undo(View v) {
        view.reverseState();
    }

    public void redo(View v) {
        view.forwardState();
    }
}


//Todo: Interpolation between circles (draw lines?)
//Todo: Use a SeekBarListener
//Todo: Come up with a solution for colors (using one or more SeekBars?)
//Todo: Make menu/buttons look nice!
//Todo: Graphic in menu to display pen size when changing it!
//Todo: Save/load from/to phone memory!
//Todo: Fill tool!
//Todo: Use a custom drawable to add a border to the menu
//Todo: Make the menu look nice!
//Todo: Use AsyncTask for drawing vs displaying!
