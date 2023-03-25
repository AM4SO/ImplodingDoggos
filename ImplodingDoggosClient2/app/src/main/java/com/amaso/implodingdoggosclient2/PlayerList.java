package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayerList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerList.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerList newInstance(String param1, String param2) {
        PlayerList fragment = new PlayerList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_list, container, false);
    }
}

class PlayerListViewAdapter extends RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder>{
    private ArrayList<CharSequence> chatMessages;
    private RecyclerView parent;

    public void setPlayers(ArrayList<Player> mess){//TODO: create actual player object for this use.
        chatMessages.add("");
        this.notifyDataSetChanged();
        parent.scrollBy(0,1000);
    }

    public ChatViewAdapter(){
        chatMessages = new ArrayList<CharSequence>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view){
        super.onAttachedToRecyclerView(view);
        parent = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_chat_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView view = holder.getTextView();
        view.setText("dfdf");
        view.setPlayerObject("dfds");
        //view.setText(chatMessages.get(position)); -- get().
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private String playerObject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt_chatItemMessageText);
        }
        public void setPlayerObject(String plrObj){
            playerObject = plrObj;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}