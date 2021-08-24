package com.example.tges;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    ImageView SplashLogo;
    private ObjectAnimator animatorAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar();
        setContentView(R.layout.activity_splash);
        SplashLogo = findViewById(R.id.SplashLogo);
        animacion("alpha");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MenuPrincipal.class);
                startActivity(intent);
                finish();

            }
        }, 1400);
    }


    private void animacion(String animacion) {
        switch (animacion){
            case "alpha":
                animatorAlpha = ObjectAnimator.ofFloat(SplashLogo, View.ALPHA, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f);
                animatorAlpha.setDuration(3000);
                AnimatorSet animatorSetAlpha= new AnimatorSet();
                animatorSetAlpha.play(animatorAlpha);
                animatorSetAlpha.start();
                break;
            default:
                break;
        }
    }
}