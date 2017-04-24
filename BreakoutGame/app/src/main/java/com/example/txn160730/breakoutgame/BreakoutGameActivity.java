package com.example.txn160730.breakoutgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class BreakoutGameActivity extends Activity {
    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new BreakoutGameView(this));
    }
    class BreakoutGameView extends SurfaceView implements Runnable, SurfaceHolder.Callback{
        Thread thread;
        volatile boolean isPlaying = true;
        SurfaceHolder surfaceHolder;  // 用于控制surfaceview
        Canvas canvas;//声明画布
        Paint paint;//声明画笔
        int screenX, screenY;
        Paddle paddle;
        Ball ball;
        Bricks[] bricks = new Bricks[200];
        int numBricks = 30;
        public BreakoutGameView(Context context){
            super(context);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            paint = new Paint();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);

        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder){
            thread = new Thread(this);
            isPlaying = true;
            thread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void run() {
            while(isPlaying){
                draw();
            }
        }
        public void pause(){
            isPlaying = false;
            while(true){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        public void resume(){
            isPlaying = true;
            thread = new Thread(this);
            thread.start();
        }


        public void draw(){
            if(surfaceHolder.getSurface().isValid()){
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.argb(255,26,128,182));
                paint.setColor(Color.argb(255,255,255,255));
                surfaceHolder.unlockCanvasAndPost(canvas);
                canvas.drawRect(paddle.getRectF(), paint);
                canvas.drawRect(ball.getRectF(), paint);
                paint.setColor(Color.argb(255,255,106,106));
                for(int i = 0; i < numBricks; i++){
                    if(bricks[i].getVisibility()){
                        canvas.drawRect(bricks[i].getRectF(), paint);
                    }
                }

            }
        }

    }
}
