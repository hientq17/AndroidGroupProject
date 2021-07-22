package edu.fpt.groupproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.model.message.MessageDetail;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private List<MessageDetail> list;
    private Context context;

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMsg;

        public MessageViewHolder(View view) {
            super(view);
            txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        }
    }

    public MessageRecyclerViewAdapter(Context context, List<MessageDetail> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT){
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_right, parent, false);
            return new MessageRecyclerViewAdapter.MessageViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_left, parent, false);
            return new MessageRecyclerViewAdapter.MessageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder( MessageRecyclerViewAdapter.MessageViewHolder holder, int position) {
        holder.txtMsg.setText(String.valueOf(list.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position){
        if(list.get(position).getType().equals("SEND")){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
