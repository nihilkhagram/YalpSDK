package com.cointizen.streaming.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import com.cointizen.paysdk.R;
import com.cointizen.streaming.bean.StreamingChannel;

public class ChannelsActivityAdapter extends RecyclerView.Adapter<ChannelsActivityAdapter.ViewHolder> {

    OnChannelClick onChannelClick;
    List<StreamingChannel> channelsList;
    Context context;

    public ChannelsActivityAdapter(Context context, List<StreamingChannel> channelsList, ChannelsActivityAdapter.OnChannelClick onChannelClick) {
        this.context = context;
        this.channelsList = channelsList;
        this.onChannelClick = onChannelClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_channels, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StreamingChannel streamingChannel = channelsList.get(position);
        holder.channelName.setText(streamingChannel.getTitle());

        holder.channelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChannelClick.onChannelClick(streamingChannel.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return channelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView channelName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.tv_channel_name);
        }
    }

    public interface OnChannelClick {
        void onChannelClick(String userId);
    }

}

