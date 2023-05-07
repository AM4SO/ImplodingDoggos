package com.amaso.implodingdoggosclient2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gameServer.GameServer;

public class LanWaitingRoom extends AppCompatActivity {
    GameHandler gameHandler;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    public LanWaitingRoom() {
        gameHandler = GameHandler.gameHandler;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        setContentView(R.layout.fragment_lan_waiting_room);

        findViewById(R.id.btn_startGame).setClickable(GameHandler.gameMaker != null);
        findViewById(R.id.btn_startGame).setOnClickListener((v) -> {
            Log.i("LAN waiting room", "Start game button pressed");
            GameServer.startNewThread(() -> GameHandler.gameMaker.startGame());
            findViewById(R.id.btn_startGame).setClickable(false);
            startActivity(new Intent(v.getContext(),Game.class));
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}