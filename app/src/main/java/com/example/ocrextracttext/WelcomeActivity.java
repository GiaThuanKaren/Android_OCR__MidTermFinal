package com.example.ocrextracttext;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;

import java.io.File;
import java.util.Calendar;


public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    View first, second, third, four, five, six;
    ImageView logo;
    Animation topA, middleA, bottomA;
    TextView slogan;
    public void CreateDirectoryExample1() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AndroidOCRFolder/";
        File root = new File(rootPath);
        if (!root.exists()) {

            Log.d("annouc","Chua co" + root.mkdir());
        }else{
            Log.d("annouc","Da Tao");
        }
//        if (folder.exists())
//                return true;
//        else//from   ww w  . j av  a  2s.  c om
//                return folder.mkdirs();
    }
    private void DeleteKeyPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("trang_thai",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key_user",null);
        editor.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        // Set color main in Navbar
        if (Build.VERSION.SDK_INT >= 21) getWindow().setNavigationBarColor(getResources().getColor(R.color.main));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.main));
        }

        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        four = findViewById(R.id.four_line);
        five = findViewById(R.id.five_line);
        six = findViewById(R.id.six_line);
        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.content);

        topA = AnimationUtils.loadAnimation(this, R.anim.top);
        middleA = AnimationUtils.loadAnimation(this, R.anim.middle);
        bottomA = AnimationUtils.loadAnimation(this, R.anim.bottom);

        first.setAnimation(topA);
        second.setAnimation(topA);
        third.setAnimation(topA);
        four.setAnimation(topA);
        five.setAnimation(topA);
        six.setAnimation(topA);
        logo.setAnimation(middleA);
        slogan.setAnimation(bottomA);
//        DeleteKeyPreference();
        // Splash screen timer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("trang_thai", MODE_PRIVATE);
                CreateDirectoryExample1();
                if (sharedPreferences.getString("key_user", "") == "") {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity1.class);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN);
    }

}