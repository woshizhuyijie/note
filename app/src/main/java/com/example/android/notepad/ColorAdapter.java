package com.example.android.notepad;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Arrays;

public  class ColorAdapter extends BaseAdapter {
        private Context context;
        private String[] colors;

        public ColorAdapter(Context context, String[] colors) {
            this.context = context;
            this.colors = colors.clone();
            //Log.d("ColorAdapter", "Colors array: " + Arrays.toString(colors));
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 每个颜色块的布局
            if (convertView == null) {
                convertView = new View(context);
            }
            Log.d("ColorAdapter", "Position: " + position + " Color: " + colors[position]); // 打印每个颜色
            convertView.setLayoutParams(new GridView.LayoutParams(200, 200)); // 设置每个颜色块的大小
            convertView.setBackgroundColor(Color.parseColor(colors[position])); // 设置颜色背景
            final int finalPosition = position;
            // 给颜色块添加点击效果
            /*convertView.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 更新颜色背景（如果需要动态处理）
                    //v.setBackgroundColor(Color.parseColor(colors[position]));
                    String selectedColor = colors[finalPosition];
                    Log.d("NoteEditor",Arrays.toString(colors));
                    Log.d("NoteEditor", "click"+finalPosition);
                    updateNoteColorBackground(colors[finalPosition]);
                }
            });*/

            return convertView;
        }
    }