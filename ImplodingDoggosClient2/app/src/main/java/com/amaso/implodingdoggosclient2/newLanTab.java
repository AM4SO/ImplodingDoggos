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

import gameServer.GameMaker;
import gameServer.Main;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        String gameName = (String)gameNameText.getText();
        if (gameName == "")
            gameName = "Some unnamed game";
        int numAiPlayers = tryParseInt((String)numAiPlayersText.getText());
        if (numAiPlayers < 0) numAiPlayers = 1;
        int maxHumanPlayers = tryParseInt((String)maxHumanPlayersText.getText());
        if ((maxHumanPlayers < 1) || (maxHumanPlayers <= 1 && numAiPlayers < 1))
            maxHumanPlayers = 2;
        String joinPassword = (String)joiningPasswordText.getText();
        boolean explodingKittensExpansion = explodingKittensCheck.isChecked();
        boolean streakingDoggosExpansion = explodingKittensCheck.isChecked();
        boolean meowingDoggosExpansion = explodingKittensCheck.isChecked();
        int expansionPack = (explodingKittensExpansion ? 1:0) + (streakingDoggosExpansion ? 1:0)<<1 + (meowingDoggosExpansion ? 1:0) << 2;

        gameMaker = new GameMaker(25565, gameName, expansionPack, numAiPlayers, maxHumanPlayers, joinPassword);
        startActivity(new Intent(this.getContext(), Game.class));
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