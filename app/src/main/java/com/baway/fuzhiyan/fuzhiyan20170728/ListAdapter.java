package com.baway.fuzhiyan.fuzhiyan20170728;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 * time:
 * author:付智焱
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<UserBean.DataBean> list=new ArrayList<>();

    public ListAdapter(Context context, List<UserBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        private TextView userName,userAge,occupation,introduction;
        private ImageView userImage;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(context,R.layout.list_item,null);
            vh.userName= (TextView) convertView.findViewById(R.id.text_userName);
            vh.userAge= (TextView) convertView.findViewById(R.id.text_userAge);
            vh.introduction= (TextView) convertView.findViewById(R.id.text_introduction);
            vh.occupation= (TextView) convertView.findViewById(R.id.text_occupation);
            vh.userImage= (ImageView) convertView.findViewById(R.id.userImage);
//            Glide.with(context).load(list.get(position).getUserImg()).into(vh.userImage);

            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        x.image().bind(vh.userImage,list.get(position).userImg);
        vh.userName.setText(list.get(position).userName);
//        vh.userAge.setText(list.get(position).getUserAge());
        vh.occupation.setText(list.get(position).occupation);
        vh.introduction.setText(list.get(position).introduction);

        vh.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+list.get(position).introduction, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
