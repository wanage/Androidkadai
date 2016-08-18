package com.app05.app05;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;

public class MyView extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myview);
    }

    public void onButton2( View v){
        Intent intent = new Intent(this,MainActivity.class); // 画面指定
        startActivity(intent);                          //  画面を開く
    }

}
