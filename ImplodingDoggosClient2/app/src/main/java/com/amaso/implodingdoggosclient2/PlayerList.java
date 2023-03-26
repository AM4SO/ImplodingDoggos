package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gameServer.Player;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerList extends Fragment {
    RecyclerView playerListView;
    PlayerListViewAdapter adapter;
    public PlayerList() {
        adapter = new PlayerListViewAdapter();
        GameHandler.gameHandler.plrList = this;
    }
    // TODO: Rename and change types and number of parameters
    public static PlayerList newInstance(String param1, String param2) {
        PlayerList fragment = new PlayerList();
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
        View ret = inflater.inflate(R.layout.fragment_player_list, container, false);;

        playerListView = ret.findViewById(R.id.view_PlayerListView);
        playerListView.setLayoutManager(new LinearLayoutManager(getContext()));
        playerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return ret;
    }
}

class PlayerListViewAdapter extends RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder>{
    private ArrayList<ClientSidePlayer> players;
    private RecyclerView parent;

    public void setPlayers(ArrayList<ClientSidePlayer> arr) {//TODO: create actual player object for this use.
        new Handler(Looper.getMainLooper()).post(() ->{
            players = arr;
            this.notifyDataSetChanged();
            parent.scrollBy(0, 1000);
        });
    }

    public PlayerListViewAdapter(){
        players = GameHandler.gameHandler.players;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view){
        super.onAttachedToRecyclerView(view);
        parent = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: Create new item so not used text_chat_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_chat_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView view = holder.getTextView();
        ClientSidePlayer player = players.get(position);

        view.setText(player.playerDetails.playerName);
        //view.setText(chatMessages.get(position)); -- get().
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt_chatItemMessageText);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}