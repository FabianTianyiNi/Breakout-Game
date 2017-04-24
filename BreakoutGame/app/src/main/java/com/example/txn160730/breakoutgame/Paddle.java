package com.example.txn160730.breakoutgame;

import android.graphics.RectF;

/**
 * Created by txn160730 on 4/23/2017.
 */

public class Paddle {
    private RectF rectF;
    private float height;
    private float width;
    private float x;
    private float y;

    /// <summary>
    ///
    /// </summary>
    /// <param name="screenX">screenX stands for the screen width</param>
    /// <param name="screenY">screenY stands for screen height</param>
    public Paddle(float screenX, float screenY){
        height = 20;
        width = 200;
        x = (screenX - width) / 2; //left
        y = screenY - 20; //top
        rectF = new RectF(x, y, x+width, y-height);
    }

    public RectF getRectF(){
        return this.rectF;
    }
}
