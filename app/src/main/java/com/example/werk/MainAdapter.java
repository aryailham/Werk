package com.example.werk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] catTitle;
    private int[] catImage;

    public MainAdapter(Context c, String[] catTitle, int[] catImage)
    {
        context = c;
        this.catTitle = catTitle;
        this.catImage = catImage;
    }

    @Override
    public int getCount() {
        return catTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater ==  null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.grid_category, null);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        imageView.setImageResource(catImage[position]);
        textView.setText(catTitle[position]);

        return convertView;
    }
}
