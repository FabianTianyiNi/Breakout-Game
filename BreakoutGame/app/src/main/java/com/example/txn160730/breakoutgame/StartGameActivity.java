package com.example.txn160730.breakoutgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by txn160730 on 4/16/2017.
 */

public class StartGameActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_game);
    }
    final Button gamebutton = (Button)findViewById(R.id.button);
//    gamebutton.setOnClickListener(new View.OnClickListener(){
//        @Override
//        public void onClick(View view){
//            Intent intent = new Intent(StartGameActivity.this, BreakoutGameActivity.class);
//
//        }
//    }


}
