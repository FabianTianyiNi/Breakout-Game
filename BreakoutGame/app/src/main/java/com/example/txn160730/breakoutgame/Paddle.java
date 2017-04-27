package com.example.txn160730.breakoutgame;

import android.graphics.RectF;

/**
 * Created by txn160730 on 4/23/2017.
 */

public class Paddle {
    private RectF rectF;
    private float height;
    private float width;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private float centerX;
    private float centerY;

    public final int LEFT_BOUNCE = 0;
    public final int RIGHT_BOUNCE = 1;
    public final int STOPPED = 2;

    private int movingstate = STOPPED;

    /// <summary>
    ///
    /// </summary>
    /// <param name="screenX">screenX stands for the screen width</param>
    /// <param name="screenY">screenY stands for screen height</param>
    public Paddle(float screenX, float screenY){
        height = 20;
        width = 130;
        left = (screenX - width)/2;
        right = left + width;
        top = screenY - height;
        bottom = screenY;
        centerX = right - width / 2;
        centerY = bottom - height / 2;
        rectF = new RectF(left, top, right, bottom);
    }

    public RectF getRectF(){
        return this.rectF;
    }
//    public void reset(int x, int y){
//        rectF.left = (x - width) / 2;
//        rectF.top = y - 20;
//        rectF.right = (x - width) / 2 + width;
//        rectF.bottom = y - 20 - height;
//    }
    public void setPaddleMovementState(int state){
        movingstate = state;
    }

    public void updateAndStartToBounce(long dps){
        if(movingstate == LEFT_BOUNCE){

        }
    }

    public void update(long dps){}
}
