package com.techkshetrainfo.cltenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences pref;
    private SessionManager session;

    private Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new SessionManager(getApplicationContext());
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.activity_splash_screen);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(50);
                        waited += 50;
                    }

                    if (session.isLoggedIn()) {
                        // User is already logged in. Take him to main activity

                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Intent intent = new Intent(SplashScreen.this,
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }

}