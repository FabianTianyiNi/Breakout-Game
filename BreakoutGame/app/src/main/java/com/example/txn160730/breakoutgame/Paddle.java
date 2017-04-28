package com.example.txn160730.breakoutgame;

import android.graphics.RectF;

/**
 *  Created by fxz160630 on 4/23/2017.
 */

public class Paddle {
    private RectF rectF;
    private float height;
    private float width;
    private float left;
    private float right;
    private float top;
    private float bottom;
    public float x;
    public float y;
    private int moveState;
    private float movingSpeed;

    public final int LEFT_BOUNCE = 0;
    public final int RIGHT_BOUNCE = 1;
    public final int STOPPED = 2;

    //定义paddle移动方向
    public final int MOVE_STOP = 0;
    public final int MOVE_LEFT = 1;
    public final int MOVE_RIGHT = 2;

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
        x = (screenX - width)/2;
        y = screenY - height;
        movingSpeed = 200;
        rectF = new RectF(left, top, right, bottom);
    }

    public RectF getRectF(){
        return this.rectF;
    }
    public void reset(float screenX, float screenY){
        this.height = 20;
        rectF.left = (screenX - width)/2;
        rectF.top = screenY - height;
        rectF.right = left + width;
        rectF.bottom = screenY;
//        rectF = new RectF(left, top, right, bottom);
    }
    public void setPaddleMovementState(int state){
        moveState = state;
    }

    public void updateAndStartToBounce(long dps){
        if(moveState == LEFT_BOUNCE){

        }
    }
    //Created by fxz160630
    public void update(long dps){
        if(this.moveState == MOVE_LEFT){
            x = x - movingSpeed/dps;
        }
        else if(this.moveState == MOVE_RIGHT){
            x = x + movingSpeed/dps;
        }
        rectF.left = x;
        rectF.right = x + this.width;
    }
    //Created by fxz160630
    public float getX(){
        return this.x;
    }
    public void setX(float x){
        this.x = x;
    }
    public float getY(){
        return this.y;
    }
    public float getWidth(){
        return width;
    }
    public void setHeight(float height){
        this.height = height;
    }

}
