package com.example.asfalis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TextAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;

    private List<String> mStrings;

    private int mViewResourceId;

    public TextAdapter(Context ctx, int viewResourceId, List<String> strings){
        super(ctx, viewResourceId, strings);

        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mStrings = strings;

        mViewResourceId = viewResourceId;

    }

    @Override
    public String getItem(int position){
        return mStrings.get(position);
    }

    @Override
    public int getCount(){
        return mStrings.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = mInflater.inflate(mViewResourceId, null);

        //TextView tv = (TextView)convertView.findViewById(R.id.@android:id/text1);
        //tv.setText(mStrings.get(position));

        return convertView;
    }

}
