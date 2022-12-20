package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cointizen.paysdk.Channels;
import com.cointizen.paysdk.R;

import java.util.List;


public class ChannelsActivityAdapter extends RecyclerView.Adapter<ChannelsActivityAdapter.ViewHolder> {

    OnChannelClick onChannelClick;
    List<Channels> channelsList;
    Context context;

    public ChannelsActivityAdapter(Context context, List<Channels> channelsList, OnChannelClick onChannelClick) {
        this.context = context;
        this.channelsList = channelsList;
        this.onChannelClick = onChannelClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channels channels = channelsList.get(position);
        holder.channelName.setText(channels.getTitle());
        Log.e("MyLogData", "ViewCount" + channelsList.get(position).getViewcount());

        holder.txtViewCount.setText(String.valueOf(channelsList.get(position).getViewcount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChannelClick.onChannelClick(channels.getId(), channels.getUser_id());
            }
        });
    }


    @Override
    public int getItemCount() {
        return channelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView channelName;
        TextView txtViewCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.tv_channel_name);
            txtViewCount = itemView.findViewById(R.id.tx_view_count);
        }
    }

    public interface OnChannelClick {
        void onChannelClick(String userId, String deletChannelId);
    }

}

