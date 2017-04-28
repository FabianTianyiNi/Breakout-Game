package com.example.txn160730.breakoutgame;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by fxz160630 on 4/23/2017.
 */

public class Ball {
    private RectF rectF;
    private float ballHeight = 20;
    private float ballWidth = 20;
    public float left;
    public float right;
    public float top;
    public float bottom;
    private float yVelocity;
    private float xVelocity;
    private float x, y;
    private int radius = 10;
// Created by fxz160630
    public Ball(float screenX, float screenY){
        this.left = (screenX-ballWidth)/2;
        this.top = screenY - 20 - ballHeight;
        this.right = left + ballWidth;
        this.bottom = screenY - 20;
        rectF = new RectF(left, top, right, bottom);
        xVelocity = 200;
        yVelocity = -400;
        this.x = (left + right) / 2;
        this.y = (bottom + top) / 2;

    }
    public RectF getRectF(){
        return this.rectF;
    }
    // Created by fxz160630
    public void reverseSpeedX(){
        xVelocity = -xVelocity;
    }
    public void reverseSpeedY(){
        yVelocity = -yVelocity;
    }
    public void reset(float x, float y){
        left = (x - ballWidth) / 2;
        top = y - 20 - ballHeight;
        right = left + ballWidth;
        bottom = y-20;
        this.x = (left + right) / 2;
        this.y = (bottom + top) / 2;
    }
    public float getcx(){
        return this.x;
    }
    public float getcy(){
        return this.y;
    }
    public void setxVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);
        if(answer == 0){
            reverseSpeedX();
        }

    }
    public int getRadius(){
        return this.radius;
    }
    //控制小球每次移动的像素位置
    public void update(long fps){
        this.x = this.x + (xVelocity/fps);
        this.y = this.y + (yVelocity/fps);
    }
    public void clearObstacleY(float y){
        rectF.bottom = y;
        rectF.top = y - ballHeight;
    }

    public void clearObstacleX(float x) {
        rectF.left = x;
        rectF.right = x + ballWidth;
    }
}
