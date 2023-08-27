package com.example.noteapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class NoteItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private Paint paint = new Paint();
    private Context context;


    public NoteItemTouchHelperCallback(Context context) {
        this.context = context;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags); // Hàm định nghĩa cho phép người dùng kéo sang phải hoặc trái
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView
            , @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX/5, dY, actionState, isCurrentlyActive);

        //Đang vuốt mục tiêu
        float translationX = dX;
        View itemView = viewHolder.itemView;
        float height = (float)itemView.getBottom() - (float)itemView.getTop();

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) // Swiping Left
        {
            translationX = -Math.min(-dX, height * 2);
            paint.setColor(Color.RED);
            RectF background = new RectF((float)itemView.getRight() + translationX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom());
            c.drawRect(background, paint);

            //viewHolder.ItemView.TranslationX = translationX;
        }
//               Log.d("database",  String.valueOf(intrinsicHeight)
//                        +" " + String.valueOf(intrinsicWidth));



    }
}
