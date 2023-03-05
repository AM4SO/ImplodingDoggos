package com.amaso.implodingdoggosclient2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

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
    public Chat() {
        chatHandler = new ChatViewAdapter();
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
            CharSequence message = chatInput.getText();
            if (message.length() == 0 ) return;
            if (message.length() > MAX_MESSAGE_LENGTH){
                message = message.subSequence(0, MAX_MESSAGE_LENGTH-1);
            }
            addMessage("You: " + message);
            chatInput.setText("");
        });

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();

        addMessage("TESTING: Testing message");
        addMessage("TESTING: Some more testing");
    }

    public void addMessage(CharSequence message){
        chatHandler.addMessage(message);//chatHandler.createViewHolder(this.getView().findViewById(R.id.view_chatView),0);
    }
}

class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder>{
    private ArrayList<CharSequence> chatMessages;
    private RecyclerView parent;

    public void addMessage(CharSequence mess){
        chatMessages.add(mess);
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
        view.setText(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
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