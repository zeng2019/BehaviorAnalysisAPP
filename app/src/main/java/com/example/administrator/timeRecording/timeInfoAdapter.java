package com.example.administrator.timeRecording;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class timeInfoAdapter extends RecyclerView.Adapter<timeInfoAdapter.ViewHolder> {

    private List<Map<String,Object>> mTimeRecList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View timeInfoView;
        TextView timeRecord;

        public ViewHolder(View view) {
            super(view);
            timeInfoView = view;
            timeRecord = (TextView) view.findViewById(R.id.timeInfo_item);
        }
    }

    public timeInfoAdapter(List<Map<String,Object>> timeRecList) {
        mTimeRecList = timeRecList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeinfo_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

//        holder.timeInfoView.setOnClickListener(new View.onClickListener(){
//            public void OnClick(View v) {
//                int position = holder.getAdapterPosition();
//                Toast.makeText(v.getContext(),"你点击了： "+mTimeRecList.get(position).get("recTime").toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String recUser;
        String recTime;
        String recSN;
        String recLogitude;
        String recLatitude;
        String timeInfo;
        recUser = mTimeRecList.get(position).get("recEmail").toString();
//        recTime = mTimeRecList.get(position).get("recTime").toString();
        recSN = mTimeRecList.get(position).get("recNodeSN").toString();
//        recLatitude = mTimeRecList.get(position).get("recLatitude").toString();
//        recLogitude = mTimeRecList.get(position).get("recLogitude").toString();
        timeInfo = recUser + "    "+recSN;
        holder.timeRecord.setText(timeInfo);

    }

    @Override
    public int getItemCount() {
        return mTimeRecList.size();
    }

}
