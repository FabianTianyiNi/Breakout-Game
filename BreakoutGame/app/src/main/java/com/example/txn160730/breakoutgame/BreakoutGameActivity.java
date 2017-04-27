package com.example.txn160730.breakoutgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

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
        boolean paused = false;
        volatile boolean isPlaying = true;
        SurfaceHolder surfaceHolder;  // 用于控制surfaceview
        Canvas canvas;//声明画布
        Paint paint;//声明画笔
        int screenX, screenY;
        Paddle paddle;
        Ball ball;
        Bricks[] bricks = new Bricks[24];
        //List<Bricks> bricks = new ArrayList<Bricks>();
        long timeThisFrame;
        int numBricks = 0;
        int brickWidth;
        int brickHeight;
//        float paddleMiddle;
//        float ballMiddle;
        long fps;
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
            brickWidth = screenX / 8;
            brickHeight = screenY / 10;
            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);
//            paddleMiddle = (paddle.getRectF().left + paddle.getRectF().right) / 2;
//            ballMiddle = ball.getcx();

            createSurfaceAndRestart(); //game start

        }
        public void createSurfaceAndRestart(){
            //ball reset
            ball.reset(screenX, screenY);
            //paddle reset
            //paddle.reset(screenX, screenY);
            //set up the bricks
            numBricks = 0;
            for(int column = 0; column < 8; column++) {
                for (int row = 0; row < 3; row++) {
                    bricks[numBricks] = new Bricks(row, column, brickWidth, brickHeight);
                    //bricks.add(new Bricks(row, column, brickWidth,brickHeight));
                    numBricks++;
                }
            }
            //live and score reset
        }
        public void update(){
            //update the ball status
            ball.update(fps);
            paddle.update(fps);
            //check if the ball collide with the bricks
            for(int i = 0; i<numBricks; i++){
                if(bricks[i].getVisibility()){
                    if(bricks[i].getRectF().intersect(ball.getcx()-ball.getRadius(),ball.getcy()-ball.getRadius(),
                            ball.getcx() + ball.getRadius(), ball.getcy()+ball.getRadius())){
                        bricks[i].setVisibility();
                        ball.reverseSpeedY();
                    }
                }
            }
            //check if the ball collide with the paddle
            if(intersect(ball, paddle.getRectF())){
                ball.setxVelocity();
                ball.reverseSpeedY();
            }

            //check if the ball hit on the right side of the wall
            if(ball.getcx() >= screenX){
                ball.reverseSpeedX();
            }
            if(ball.getcy() <= 0){
                ball.reverseSpeedY();
            }
            if(ball.getcx() <= 0){
                ball.reverseSpeedX();
            }
            if(ball.getcy() >= screenY){
                ball.reverseSpeedY();
            }

        }

        public boolean intersect(Ball ball, RectF rectF){
            if(rectF.top - ball.getcy() == ball.getRadius()) return true;
            else if(ball.getcy() - rectF.bottom == ball.getRadius()) return true;
            else if(rectF.left-ball.getcx() == ball.getRadius()) return true;
            else if(ball.getcx() - rectF.right == ball.getRadius()) return true;
            return false;
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
                long startFrameTime = System.currentTimeMillis();
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if(timeThisFrame >= 1){
                    fps = 1000/timeThisFrame;
                }
                if(!paused){
                    update();
                }
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
                canvas.drawRect(paddle.getRectF(), paint);
                //canvas.drawRect(ball.getRectF(), paint);
                canvas.drawCircle(ball.getcx(),ball.getcy(), 10,paint);
                paint.setColor(Color.argb(255,255,106,106));
                for(int i = 0; i < numBricks; i++){
                    if(bricks[i].getVisibility()){
                        canvas.drawRect(bricks[i].getRectF(), paint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    ball.setxVelocity();
//                    if(motionEvent.getX() > screenX / 2){
//                        paddle.setPaddleMovementState(paddle.RIGHT_BOUNCE);
//                    }else{
//                        paddle.setPaddleMovementState(paddle.LEFT_BOUNCE);
//                    }
                    break;
                case MotionEvent.ACTION_UP:
                    paddle.setPaddleMovementState(paddle.STOPPED);
            }
            return true;
        }

    }
}
