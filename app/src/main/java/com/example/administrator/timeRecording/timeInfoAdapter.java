package com.example.administrator.timeRecording;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Date date = null;
        String recSN;
        String recLogitude;
        String recLatitude;
        String timeInfo;
        String posName;
        recUser = mTimeRecList.get(position).get("recEmail").toString();
        //时间格式化处理
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = fmt.parse(mTimeRecList.get(position).get("recTime").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recTime = fmt.format(date);
        recSN = mTimeRecList.get(position).get("recNodeSN").toString();
//        recLatitude = mTimeRecList.get(position).get("recLatitude").toString();
//        recLogitude = mTimeRecList.get(position).get("recLogitude").toString();
//        根据SN，检索nodeInfo数据表，取得位置信息
        if(recSN.equals("0117C5976A3E"))
            posName = "图书馆";
        else if (recSN.equals("0117C597055B"))
            posName = "工科教学楼";
        else if (recSN.equals("0117C5976771"))
            posName = "宿舍楼";
        else
            posName = "未知位置！";

        timeInfo =position + ":    " + recTime + "    "+posName;
        holder.timeRecord.setText(timeInfo);
    }

    @Override
    public int getItemCount() {
        return mTimeRecList.size();
    }

}
