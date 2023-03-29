package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class PlayerView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_ID = "playerId";

    // TODO: Rename and change types of parameters
    private int playerId;

    ClientSidePlayer player;
    int thisTurnNumber;
    int plrGo = 0;
    private int width,height;

    public PlayerView() {
    }
    public void changeTurn(int turn){
        plrGo = turn;
    }
    /*public void setPlayer(int playerId){
    }*/

    public static PlayerView newInstance(int playerId) {
        PlayerView fragment = new PlayerView();
        Bundle args = new Bundle();
        args.putInt(PLAYER_ID, playerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments() != null) {
            playerId = getArguments().getInt(PLAYER_ID);
            player = ClientSidePlayer.getPlayerByPlayerId(playerId);
            assert player != null;
        }
    }
    private void updatePos(View view){

        new Thread(() -> {
            ViewPropertyAnimator animator = view.animate();
            double c = (4*Math.PI / 6);//GameHandler.gameHandler.players.size());
            while (true){
                double t = Math.PI + (plrGo+thisTurnNumber) * c;
                double x = x(t);
                double y = y(t);
                this.getActivity().runOnUiThread(() -> {
                    animator.setDuration(2000);
                    animator.translationX((float)x).translationY((float)y).start();
                });
                plrGo++;
                try {
                    Thread.sleep(animator.getDuration());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private double y(double t){
        return 150 * (-1 * Math.sin(0.5*t) + 0.8);
    }
    private double x(double t){
        return 150 * (-3 * Math.cos(0.5*t) + 2.6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_view, container, false);
        TextView textView = view.findViewById(R.id.txt_plrName);
        textView.setText(player.playerDetails.playerName);
        width = view.getWidth();
        height = view.getHeight();
        Log.i(String.valueOf(width),String.valueOf(height));
        
        updatePos(view);

        return view;
    }
}