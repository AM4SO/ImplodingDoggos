package com.amaso.implodingdoggosclient2;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    static PlayerListTurnView ActiveView;

    ArrayList<ClientSidePlayer> players;
    ArrayList<PlayerView> playerViews;
    int playerTurn = 0;
    public PlayerListTurnView() {
        ActiveView = this;
        playerViews = new ArrayList<>();
        players = GameHandler.gameHandler.players;
        /// foreach player: instantiate fragment

    }
    public void addPlayerView(ClientSidePlayer player){
        PlayerView plrView = PlayerView.newInstance(player.playerState.playerId);
        playerViews.add(plrView);
        plrView.thisTurnNumber = playerViews.size()-1;

        FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.playerContainer, plrView);
        fragmentTransaction.commit();
    }

    public void playerAdded(ClientSidePlayer player){
        addPlayerView(player);
    }

    public void changePlayerTurn(int playerTurn){
        this.playerTurn = playerTurn;
        for(PlayerView plrView : playerViews){
            plrView.changeTurn(playerTurn);
        }
    }

    public static PlayerListTurnView newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_player_list_turn_view, container, false);

        for (ClientSidePlayer player : players){
            addPlayerView(player);
        }
        return view;
    }
}