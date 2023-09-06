package com.zjy.okhttpnew.lv;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjy.okhttpnew.R;

import java.util.List;

/**
 * Date:2023/9/6 11:06
 * author:jingyu zheng
 */
public class StudentAdapter extends BaseAdapter {
    public List<Student>list;
    public Context context;

    public StudentAdapter(Context context, List<Student>list){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.lv_item_layout,null);

            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvAge = convertView.findViewById(R.id.tvAge);
            //将holder放入当前视图中
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Student student = list.get(position);
        Log.e("===",student.toString());
        viewHolder.tvName.setText(student.getName());
        viewHolder.tvAge.setText(student.getAge()+"");

        return convertView;
    }
    public class ViewHolder {
        TextView tvName;
        TextView tvAge;
    }
}
