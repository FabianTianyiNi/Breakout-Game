package com.example.txn160730.breakoutgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Created by txn160730
public class BreakoutGameActivity extends Activity {
    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(new BreakoutGameView(this));
    }
    // Created by txn160730
    class BreakoutGameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
        Thread thread;
        boolean paused = true;
        volatile boolean isPlaying = true;
        SurfaceHolder surfaceHolder;  // 用于控制surfaceview
        Canvas canvas;//声明画布
        Paint paint;//声明画笔
        int screenX, screenY;
        Paddle paddle;
        Ball ball;
        Bricks[] bricks = new Bricks[24];
        private SensorManager sensorMgr = null;
        Sensor sensor = null;
        //List<Bricks> bricks = new ArrayList<Bricks>();
        long timeThisFrame;
        int numBricks = 0;
        int brickWidth;
        int brickHeight;
        private float paddlePosXForLeft;
        private float paddlePosXForRight;
        private float paddlePosY;
        private float sensorAxisX;
        private float sensorAxisY;
        private float sensorAxisZ;
        AlertDialog.Builder builder;
        private Context mContext;
        //        float paddleMiddle;
//        float ballMiddle;
        long fps;
        int life = 3;
        int mark = 0;

        // Created by txn160730
        public BreakoutGameView(Context context) {
            super(context);
            mContext = BreakoutGameActivity.this;
            builder=new AlertDialog.Builder(context);
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
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            paddlePosXForLeft = paddle.getRectF().left;
            paddlePosXForRight = paddle.getRectF().right;
//            paddleMiddle = (paddle.getRectF().left + paddle.getRectF().right) / 2;
//            ballMiddle = ball.getcx();

            createSurfaceAndRestart(); //game start

        }
        // Created by txn160730
        public void createSurfaceAndRestart() {
            sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
            //ball reset
            ball.reset(screenX, screenY);
            //paddle reset
            paddle.reset(screenX, screenY);
            //set up the bricks
            numBricks = 0;
            for (int column = 0; column < 8; column++) {
                for (int row = 0; row < 3; row++) {
                    bricks[numBricks] = new Bricks(row, column, brickWidth, brickHeight);
                    //bricks.add(new Bricks(row, column, brickWidth,brickHeight));
                    numBricks++;
                }
            }
            //live and score reset
        }
        // Created by txn160730
        public void update() {

            //update the ball status
            ball.update(fps);
            paddle.update(fps);
            //check if the ball collide with the bricks
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (bricks[i].getRectF().intersect(ball.getcx() - ball.getRadius(), ball.getcy() - ball.getRadius(),
                            ball.getcx() + ball.getRadius(), ball.getcy() + ball.getRadius())) {
                        bricks[i].setVisibility();
                        ball.reverseSpeedY();
                        mark++;
                    }
                }
            }
            //check if the ball collide with the paddle
            if (intersect(paddle.getRectF(),ball.getcx() - ball.getRadius(), ball.getcy() - ball.getRadius(),
                    ball.getcx() + ball.getRadius(), ball.getcy() + ball.getRadius())) {
                ball.setxVelocity();
                ball.reverseSpeedY();
                ball.clearObstacleY(paddle.getRectF().top - 2);
            }

            //check if the ball hit on the right side of the wall
            if (ball.getcx() >= screenX) {
                ball.reverseSpeedX();
            }
            //check if the ball hit on the top of the screen
            if (ball.getcy() <= 0) {
                ball.reverseSpeedY();
            }
            //check if the ball hit on the left side
            if (ball.getcx() <= 0) {
                ball.reverseSpeedX();
            }
            //check if the ball hit on the bottom of the screen
            if (ball.getcy() >= screenY) {
                //dialog();
                if(life > 0){
                    life --;
                    ball.reverseSpeedY();
                }
                else{
                    //game over
                }
            }
        }



        // Created by txn160730
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
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
            while (isPlaying) {
                long startFrameTime = System.currentTimeMillis();
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
                if (!paused) {
                    update();

                }
            }
        }
        public void pause() {
            isPlaying = false;
            while (true) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void resume() {
            isPlaying = true;
            thread = new Thread(this);
            thread.start();
        }


        public void draw() {
            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.argb(255, 26, 128, 182));
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawRect(paddle.getRectF(), paint);
                //canvas.drawRect(ball.getRectF(), paint);
                canvas.drawCircle(ball.getcx(), ball.getcy(), 10, paint);
                // 画方框
                paint.setColor(Color.argb(255, 255, 106, 106));
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        canvas.drawRect(bricks[i].getRectF(), paint);
                    }
                }
                // 画积分框
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(60);
                canvas.drawText("LIFE: " + life, 100, 80, paint);
                canvas.drawText("SCORE: " + mark, 400, 80, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        final SensorEventListener lsn = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                //更新重力加速度传感器坐标
                sensorAxisX = event.values[0];
                sensorAxisY = event.values[1];
                //sensorAxisZ = event.values[SensorManager.AXIS_Z];
                //每次移动迫使paddle移动 注意：paddle只能水平移动
                paddlePosXForLeft += sensorAxisX * 2;
                paddlePosXForRight += sensorAxisX * 2;
                //定义paddle移动方向
                if (sensorAxisX < 0) {//向右移动
                    paddle.setPaddleMovementState(paddle.MOVE_RIGHT);
                    paddle.update(fps);
                } else if (sensorAxisX > 0) { //向左移动
                    paddle.setPaddleMovementState(paddle.MOVE_LEFT);
                    paddle.update(fps);
                } else { //不动
                    paddle.setPaddleMovementState(paddle.MOVE_STOP);
                    paddle.update(fps);
                }
                //检测paddle有没有越界，先是左边界，然后是右边界
                if (paddle.getX() <= 0) {
                    paddle.x = 0;
                } else if ((paddle.getX() + paddle.getWidth()) >= screenX) {
                    paddle.x = screenX-paddle.getWidth();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        // Created by txn160730
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_UP:
                    paused = false;
                    break;
            }
            return true;
        }

        public boolean intersect(RectF rectF, float left, float top, float right, float bottom) {
            if (rectF.left < right && left < rectF.right
                    && rectF.top < bottom && top < rectF.bottom) {
                if (rectF.left < left) {
                    //this.left = left;
                }
                if (rectF.top < top) {
                    //this.top = top;
                }
                if (rectF.right > right) {
                    //this.right = right;
                }
                if (rectF.bottom > bottom) {
                    //this.bottom = bottom;
                }
                return true;
            }
            return false;
        }
        //game over appear
        public void gameoverPicsLoading(){
            ImageView imgView = new ImageView(mContext);


        }

//        private void dialog(){
//              //先得到构造器
//            builder.setTitle("GAME OVER"); //设置标题
//            builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() { //设置确定按钮
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss(); //关闭dialog
//                    Toast.makeText(BreakoutGameActivity.this, "YES" + which, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            //参数都设置完成了，创建并显示出来
//            builder.create().show();
//        }


    }
}
