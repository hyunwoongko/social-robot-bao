package com.welfarerobotics.welfareapplcation.core.settings;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.welfarerobotics.welfareapplcation.R;

import java.util.List;


public class WifiScanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WifiFragment.device> wifiList;
    private Context context;
    private View.OnClickListener mOnClickListener;

    public WifiScanAdapter(List<WifiFragment.device> wifiList, Context context) {
        this.wifiList = wifiList;
        this.context=context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.network_list, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        itemView.setTag(holder);
        itemView.setOnClickListener(mOnClickListener);
        return holder;

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        WifiFragment.device device=wifiList.get(position);
        String name=device.getName().toString();

        ((MyViewHolder) holder).vName.setText(name);
        ((MyViewHolder) holder).vName.setTag(device);


        ((MyViewHolder) holder).vImage.setImageResource(R.drawable.ic_action_wifi);
        ((MyViewHolder) holder).context = context;
        ((MyViewHolder) holder).position = position;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {

        int itemCount = wifiList.size();

        return itemCount;
    }
    public void setOnClickListener(View.OnClickListener lis) {
        mOnClickListener = lis;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImage;
        protected TextView vName;
        protected  Context context;
        protected int position;


        public MyViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.ssid_name);
            vImage = v.findViewById(R.id.Wifilogo);

        }
    }

}
