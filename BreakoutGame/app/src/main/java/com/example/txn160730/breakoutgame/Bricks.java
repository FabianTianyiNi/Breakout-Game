package com.example.txn160730.breakoutgame;

import android.graphics.RectF;

/**
 * Created by txn160730 on 4/23/2017.
 */

public class Bricks {
    private boolean visibility = true;
    private RectF rectF;
    private float width;
    private float height;
    private int padding;

    public Bricks(int row, int column, int width, int height){
        visibility = true;
        this.width = width;
        this.height = height;
        padding = 1;
        rectF = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }
    public boolean getVisibility(){
        return this.visibility;
    }
    public void setVisibility(){
        this.visibility = false;
    }

    public RectF getRectF(){
        return this.rectF;
    }
}
