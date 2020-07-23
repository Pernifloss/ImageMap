package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.domain.DemoActivity;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CustomAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final LayoutInflater layoutInflater;
    private final List<DemoActivity> items;
    private final Context ctx;

    public CustomAdapter(Context ctx, List<DemoActivity> list) {
        layoutInflater = LayoutInflater.from(ctx);
        this.items = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DemoActivity getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DemoActivityHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_demo_activity, null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder = new DemoActivityHolder(textView, imageView);
            convertView.setTag(holder);

        } else {
            holder = (DemoActivityHolder) convertView.getTag();
        }
        holder.text.setText(getItem(position).getName());
        holder.getIcon().setImageResource(getItem(position).getImageResID());

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ctx
                , getItem(position).getActivity());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    static class DemoActivityHolder {
        TextView text;
        ImageView icon;

        public DemoActivityHolder(TextView text, ImageView icon) {
            this.text = text;
            this.icon = icon;
        }

        public TextView getText() {
            return text;
        }

        public ImageView getIcon() {
            return icon;
        }
    }
}
