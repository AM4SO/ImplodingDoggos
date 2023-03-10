package com.amaso.implodingdoggosclient2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

public class Game extends AppCompatActivity {
    View thisView;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        thisView = findViewById(R.id.root);

        findViewById(R.id.fragmentContainerView5).setOnClickListener((view) -> {
            Log.i("BIG COOL KITTY", "         OUCHY THAT HURT");
        });
        View v = findViewById(R.id.fragmentContainerView5);


        PlayerView newPlr = PlayerView.newInstance("AMASO","doens't matter");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.root, newPlr);
        fragmentTransaction.commitNow();

        Thread x = new Thread(() -> moveThingy());
        x.start();
    }
    protected void moveThingy(){
        View view = findViewById(R.id.fragmentContainerView5);
        ViewPropertyAnimator animator = view.animate();
        int t = 0;
        float startTrans = view.getTranslationX();

        while ( true) {

            runOnUiThread(() -> {
                animator.translationY(200);
                animator.setDuration(2000);
                animator.start();
            });
            try {
                Thread.sleep(animator.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                animator.translationY(-200);
                animator.start();
            });
            try {
                Thread.sleep(animator.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*    double translateAmount = 400 * Math.sin((double) t/30);
            runOnUiThread(() -> {
                view.setTranslationY((float)translateAmount);
            });
            t++;
            try {
                Thread.sleep(17);//17
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}