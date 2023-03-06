package com.amaso.implodingdoggosclient2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Game extends AppCompatActivity {
    View thisView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        thisView = findViewById(R.id.root);

        LayoutInflater inflater = LayoutInflater.from(this);
        PlayerView newPlr = PlayerView.newInstance("AMASO","doens't matter");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.root, newPlr);
        fragmentTransaction.commit();
    }
}