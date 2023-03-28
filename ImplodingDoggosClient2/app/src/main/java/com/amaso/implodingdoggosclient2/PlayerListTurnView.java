package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerListTurnView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerListTurnView extends Fragment {
    ArrayList<ClientSidePlayer> players;
    int playerTurn;
    public PlayerListTurnView() {
        players = GameHandler.gameHandler.players;
        /// foreach player: instantiate fragment
    }

    public void playerAdded(ClientSidePlayer player){
        // instantiate fragment
    }

    public void changePlayerTurn(int playerTurn){
        // translate player fragments
    }

    public static PlayerListTurnView newInstance(String param1, String param2) {
        PlayerListTurnView fragment = new PlayerListTurnView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_list_turn_view, container, false);
    }
}