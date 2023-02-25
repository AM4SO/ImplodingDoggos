package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gameServer.GameServer;
import gameServer.Main;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class newLanTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_lan_tab, container, false);
    }
    @Override
    public void onResume(){
        super.onResume();
        Button startServerBtn = this.getView().findViewById(R.id.btn_hostGame);
        if (startServerBtn == null) Log.e("FRAGMENT..........:", "Failed to find button");
        else{
            startServerBtn.setOnClickListener((view) -> {
                new Thread(() -> {
                    Main.main(null);
                }).start();
            });
        }
    }
}