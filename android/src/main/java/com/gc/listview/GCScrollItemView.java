package com.gc.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.facebook.react.views.view.ReactViewGroup;

public class GCScrollItemView extends ReactViewGroup {
    private int startY = -100000;
    private int endY = -101000;

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public GCScrollItemView(Context context) {
        super(context);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
