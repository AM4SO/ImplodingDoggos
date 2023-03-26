package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {
    final private int MAX_MESSAGE_LENGTH = 300;
    private ChatViewAdapter chatHandler;
    private RecyclerView chatView;
    private TextView chatInput;
    private PlayerDetails localPlayerDetails;
    public Chat() {
        chatHandler = new ChatViewAdapter();
        GameHandler.gameHandler.chat = this;
        // Required empty public constructor
    }
    public static Chat newInstance() {
        Chat fragment = new Chat();
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
        View ret = inflater.inflate(R.layout.fragment_chat, container, false);

        chatView = ret.findViewById(R.id.view_chatView);
        chatView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatView.setAdapter(chatHandler);

        chatInput = ret.findViewById(R.id.txt_message);

        ret.findViewById(R.id.btn_sendMessage).setOnClickListener(view -> {
            CharSequence message = chatInput.getText().toString().trim();
            if (message.length() == 0) return;
            if (message.length() > MAX_MESSAGE_LENGTH){
                message = message.subSequence(0, MAX_MESSAGE_LENGTH-1);
            }
            addMessage(message);
            chatInput.setText("");
            GameHandler.gameHandler.sendChatMessage(message.toString());
        });
        localPlayerDetails = new PlayerDetails();
        localPlayerDetails.playerName = "You";

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addMessage(CharSequence message){
        chatHandler.addMessage(new ChatMessageDetails(localPlayerDetails, message.toString()));//chatHandler.createViewHolder(this.getView().findViewById(R.id.view_chatView),0);
    }
    public void addMessage(ChatMessageDetails message){
        chatHandler.addMessage(message);
    }
}

class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatItemViewHolder>{
    private ArrayList<CharSequence> chatMessages;
    private ArrayList<ChatMessageDetails> chatMessagesDetails;
    private RecyclerView parent;

    public void addMessage(CharSequence mess){
        chatMessages.add(mess);
        this.notifyDataSetChanged();
        parent.scrollBy(0, 1000);
    }
    public void addMessage(ChatMessageDetails message){
        new Handler(Looper.getMainLooper()).post(() -> {
            chatMessagesDetails.add(message);
            this.notifyDataSetChanged();
            parent.scrollBy(0, 1000);
        });
    }

    public ChatViewAdapter(){
        chatMessages = new ArrayList<CharSequence>();
        chatMessagesDetails = new ArrayList<ChatMessageDetails>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view){
        super.onAttachedToRecyclerView(view);
        parent = view;
    }

    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_chat_item,parent,false);

        return new ChatItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
        TextView view = holder.getTextView();
        ChatMessageDetails message = chatMessagesDetails.get(position);

        view.setText(message.player.playerName.concat(": ").concat(message.message));
    }

    @Override
    public int getItemCount() {
        return chatMessagesDetails.size();
    }

    class ChatItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        public ChatItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt_chatItemMessageText);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}