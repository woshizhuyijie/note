package com.example.android.notepad.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.notepad.NotePad;
import com.example.android.notepad.R;
import com.example.android.notepad.utills.DateDisplay;
import com.google.android.material.card.MaterialCardView;
public class NotesAdapter extends CursorAdapter {

    public NotesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);  // 传入context、cursor和标志（0是默认的标志，通常用于告诉适配器不需要做额外的工作）
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 获取视图中的各个组件
        TextView noteTextView = view.findViewById(R.id.text_content);  // 显示笔记内容
        TextView dateTextView = view.findViewById(R.id.text_date);  // 显示日期

        // 获取列索引
        int text = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE);
        int time = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE);
        int tag = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_COLOR_TAG);

        // 只有列索引有效时才处理数据
        if (text != -1 && time != -1 && tag != -1) {
            // 从cursor中获取数据
            String noteText = cursor.getString(text);
            long timestamp = cursor.getLong(time);  // 假设日期字段是timestamp类型
            String tagColor = cursor.getString(tag);  // 获取颜色字段

            // 设置笔记内容
            noteTextView.setText(noteText);

            // 设置日期，使用自定义的日期格式化方法
            String formattedDate = DateDisplay.showTime(timestamp);
            dateTextView.setText(formattedDate);

            // 设置颜色标签背景
            MaterialCardView cardView = view.findViewById(R.id.card_view); // 假设卡片视图有 ID card_view
            if (tagColor != null) {
                cardView.setCardBackgroundColor(Color.parseColor(tagColor)); // 设置颜色背景
            }
        } else {
            // 如果cursor没有数据，或者列索引无效，可以在这里做相应的错误处理
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 创建并返回新的视图
        return LayoutInflater.from(context).inflate(R.layout.noteslist_item, parent, false);
    }
}
