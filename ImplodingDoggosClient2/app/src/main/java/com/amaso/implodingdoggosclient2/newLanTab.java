package com.amaso.implodingdoggosclient2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import gameServer.GameMaker;
import gameServer.GameServer;
import gameServer.Main;
import gameServer.clientSide.ImplodingDoggosUser;
import gameServer.clientSide.RemoteGameAdapter;
import gameServer.clientSide.RemoteGameDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class newLanTab extends Fragment {
    public static GameMaker gameMaker;
    TextView gameNameText;
    CheckBox explodingKittensCheck;
    CheckBox streakingDoggosCheck;
    CheckBox meowingDoggosCheck;
    TextView numAiPlayersText;
    TextView maxHumanPlayersText;
    TextView joiningPasswordText;
    Inet4Address localHost;

    public void setLocalHost(){
        try {
            GameServer.startNewThread(() -> {
                try {
                    localHost = (Inet4Address) Inet4Address.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLocalHost();
        return inflater.inflate(R.layout.fragment_new_lan_tab, container, false);
    }
    public int tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            return -1;
        }
    }
    public void startServer(){
        String gameName = gameNameText.getText().toString();
        if (gameName == "")
            gameName = "Some unnamed game";
        int numAiPlayers = tryParseInt(numAiPlayersText.getText().toString());
        if (numAiPlayers < 0) numAiPlayers = 1;
        int maxHumanPlayers = tryParseInt(maxHumanPlayersText.getText().toString());
        if ((maxHumanPlayers < 1) || (maxHumanPlayers <= 1 && numAiPlayers < 1))
            maxHumanPlayers = 2;
        String joinPassword = joiningPasswordText.getText().toString();
        boolean explodingKittensExpansion = explodingKittensCheck.isChecked();
        boolean streakingDoggosExpansion = explodingKittensCheck.isChecked();
        boolean meowingDoggosExpansion = explodingKittensCheck.isChecked();
        int expansionPack = (explodingKittensExpansion ? 1:0) + (streakingDoggosExpansion ? 1:0)<<1 + (meowingDoggosExpansion ? 1:0) << 2;
        String finalGameName = gameName;
        int finalNumAiPlayers = numAiPlayers;
        int finalMaxHumanPlayers = maxHumanPlayers;
        //GameServer.startNewThread(() -> {
            GameHandler.gameMaker = new GameMaker(25565, finalGameName, expansionPack, finalNumAiPlayers, finalMaxHumanPlayers, joinPassword);
            RemoteGameAdapter gameConnection = new RemoteGameAdapter(new RemoteGameDetails(localHost,25565,0),ImplodingDoggosUser.RandomUser());
            new GameHandler(gameConnection);
        //});
        //getLayoutInflater().inflate(R.layout.fragment_lan_waiting_room, (ViewGroup) getView());
        startActivity(new Intent(this.getContext(), LanWaitingRoom.class));
    }
    @Override
    public void onResume(){
        super.onResume();
        Button startServerBtn = this.getView().findViewById(R.id.btn_hostGame);
        startServerBtn.setOnClickListener((view) -> startServer());
        /*if (startServerBtn == null) Log.e("FRAGMENT..........:", "Failed to find button");
        else{
            startServerBtn.setOnClickListener((view) -> {
                new Thread(() -> {
                    Main.main(null);
                }).start();
                startActivity(new Intent(this.getContext(),Game.class));
            });
        }*/
        gameNameText = (TextView) getView().findViewById(R.id.txt_gameName);
        explodingKittensCheck = (CheckBox) getView().findViewById(R.id.check_explodingDoggos);
        streakingDoggosCheck = (CheckBox) getView().findViewById(R.id.check_meowingDoggos);
        meowingDoggosCheck = (CheckBox) getView().findViewById(R.id.check_streakingDoggos);
        numAiPlayersText = (TextView) getView().findViewById(R.id.txt_NumBots);
        maxHumanPlayersText = (TextView) getView().findViewById(R.id.txt_maxHumanPlayers);
        joiningPasswordText = (TextView) getView().findViewById(R.id.txt_joinPassword);

    }
}