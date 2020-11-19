package com.overseer.vendassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIMER = 3000;

    //for anim
    ImageView backgroundImage;
    TextView poweredBy;
    Animation sideAnim, bottomAnim;
    SharedPreferences sharedPreferences; //for storing such as whether first time using app,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*setFlags
        TODO: understand set flags
        * to current window, set the intent as to display full screen
        * */
        setContentView(R.layout.activity_splashscreen);

        //"hooks"
        backgroundImage = findViewById(R.id.background_image);
        poweredBy = findViewById(R.id.powered_by);

        //animation instances
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //set animation
        backgroundImage.setAnimation(sideAnim);
        poweredBy.setAnimation(bottomAnim);

        //automatically goes to next screen after 5s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check if user is using app for first time
                /*sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean firstTimeUsing = sharedPreferences.getBoolean("firstTime", true);
                if (firstTimeUsing) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), SelectItems.class);
                    startActivity(intent);
                    finish();
                } else { */
                Intent nextScreen = new Intent(getApplicationContext(), Dashboard.class); //(instance you're at, where you want to go) SplashScreen.this also can
                startActivity(nextScreen);
                finish(); //destroy activity and move to next activity, so that back button will not bring you to splash screen
                //}
            }
        }, SPLASH_TIMER);

    }
}