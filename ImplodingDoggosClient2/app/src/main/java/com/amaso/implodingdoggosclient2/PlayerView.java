package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLAYER_NAME = "DEFAULT";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String playerName;
    private String mParam2;

    public PlayerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerName Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerView.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerView newInstance(String playerName, String param2) {
        PlayerView fragment = new PlayerView();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, playerName);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerName = getArguments().getString(PLAYER_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_view, container, false);
        TextView textView = view.findViewById(R.id.txt_plrName);
        textView.setText(playerName);

        return view;
    }
}