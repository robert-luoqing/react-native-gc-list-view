package com.gc.listview;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.view.ReactViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class GCCoordinatorView extends ReactViewGroup {
    private ReactScrollView scrollView;
    private ArrayList<GCNotifyView> notifyViews;

    private int forceIndex;
    private ReadableArray itemLayouts;
    private ReadableArray categories;

    private int lastScrollViewY;

    public int getForceIndex() {
        return forceIndex;
    }

    public void setForceIndex(int forceIndex) {
        this.forceIndex = forceIndex;
        this.handleScroll(scrollView, this.lastScrollViewY, true);
    }

    public ReadableArray getItemLayouts() {
        return itemLayouts;
    }

    public void setItemLayouts(ReadableArray itemLayouts) {
        this.itemLayouts = itemLayouts;
        this.handleScroll(scrollView, this.lastScrollViewY, true);
    }

    public ReadableArray getCategories() {
        return categories;
    }

    public void setCategories(ReadableArray categories) {
        this.categories = categories;
        this.handleScroll(scrollView, this.lastScrollViewY, true);
    }

    public GCCoordinatorView(Context context) {
        super(context);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (scrollView == null) {
            scrollView = GCCoordinatorView.fintSpecifyParent(ReactScrollView.class, this.getParent());
            if (scrollView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                            GCCoordinatorView.this.handleScroll(scrollView, i1, false);
                            Log.d("DEBUG", "coordinate: " + String.valueOf(i1));
                        }
                    });
                }

            }
        }
    }


    public static <T> T fintSpecifyParent(Class<T> tClass, ViewParent parent) {
        if (parent == null) {
            return null;
        } else {
            if (tClass.isInstance(parent)) {
                return (T) parent;
            } else {
                return fintSpecifyParent(tClass, parent.getParent());
            }
        }
    }

    public void registerView(GCNotifyView view) {
        if (notifyViews == null) {
            notifyViews = new ArrayList<GCNotifyView>();
        }
        notifyViews.add(view);
    }


    private void notifyToShowIndex(ArrayList<GCNotifyVisiableModel> showObjs, boolean isForce) {
        if (this.notifyViews != null) {
            ArrayList<GCNotifyView> matchedNotifyViews = new ArrayList<>(this.notifyViews);
            if (showObjs.size() > 0 && matchedNotifyViews.size() > 0) {
                int notifyCount = matchedNotifyViews.size();
                HashMap<Integer, GCNotifyView> assignedViews = new HashMap<Integer, GCNotifyView>();

                for (int notifyIndex = 0; notifyIndex < notifyCount; notifyIndex++) {
                    GCNotifyView notifyObj = matchedNotifyViews.get(notifyIndex);
                    if (notifyObj.getIndex() >= 0) {

                        assignedViews.put(notifyObj.getIndex(), notifyObj);
                    }
                }


                ArrayList<GCNotifyVisiableModel> unhandledShowObjs = new ArrayList<>();
                for (int showIndex = 0; showIndex < showObjs.size(); showIndex++) {
                    GCNotifyVisiableModel showObj = showObjs.get(showIndex);
                    int key = showObj.getIndex();
                    GCNotifyView notifyObj = assignedViews.get(key);
                    if (notifyObj != null) {
                        notifyObj.notifyRebind(showObj, isForce);
                        matchedNotifyViews.remove(notifyObj);

                    } else {
                        unhandledShowObjs.add(showObj);

                    }
                }


                for (int i = 0; i < unhandledShowObjs.size(); i++) {
                    GCNotifyVisiableModel showObj = unhandledShowObjs.get(i);
                    if (matchedNotifyViews.size() > 0) {
                        GCNotifyView notifyObj = matchedNotifyViews.get(0);
                        notifyObj.notifyRebind(showObj, isForce);
                        matchedNotifyViews.remove(notifyObj);
                    }
                }
            }

            for (int index = 0; index < matchedNotifyViews.size(); index++) {
                GCNotifyView notifyView = matchedNotifyViews.get(index);
                notifyView.emptyBind();
            }
        }
    }

    public void handleScrollOutside() {
        this.handleScroll(scrollView, this.lastScrollViewY, true);
    }

    private void handleScroll(ScrollView scrollView, int y, boolean isForce) {
        this.lastScrollViewY = y;
        int scrollHeight = 0;
        if(scrollView != null) {
            scrollHeight = convertPixelsToDp(scrollView.getHeight(), this.getContext());
        }

        if(scrollHeight<50) {
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            scrollHeight = convertPixelsToDp(height, this.getContext());
        }

        handleScrollInner(scrollHeight, convertPixelsToDp(y, this.getContext()), isForce);
    }

    /**
     *
     * @param scrollHeight the scrollHeight is dp unit
     * @param y the y is dp unit
     * @param isForce
     */
    private void handleScrollInner(int scrollHeight, int y, boolean isForce) {
        int minY = y - 50;
        if (minY < 0) {
            minY = 0;
        }

        int maxY = minY + scrollHeight + 100;

        boolean isPassed = false;
        ArrayList<GCNotifyVisiableModel> showObjs = new ArrayList<>();
        int itemCount = 0;
        if (this.itemLayouts != null) {
            itemCount = this.itemLayouts.size();
        }

        for (int i = 0; i < itemCount; i++) {
            int startY = 0;
            int endY = this.itemLayouts.getInt(i);
            if (i != 0) {
                startY = this.itemLayouts.getInt(i - 1);
            }

            // It mean the element fall in visual area
            if ((startY <= minY && endY >= minY)
                    || (startY >= minY && endY <= maxY)
                    || (startY <= maxY && endY >= maxY)
            ) {
                GCNotifyVisiableModel notifyVisiableModel
                        = new GCNotifyVisiableModel(i, startY, endY);
                // to show the item
                showObjs.add(notifyVisiableModel);
                isPassed = true;
            } else {
                if (isPassed) {
                    break;
                }
            }
        }

        this.notifyToShowIndex(showObjs, isForce);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.relayoutChildren();
    }

    public void relayoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            GCScrollItemView child = (GCScrollItemView) getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int startY = convertDpToPixels(child.getStartY(), this.getContext());
            int endY = convertDpToPixels(child.getEndY(), this.getContext());
            child.layout(0, startY, childWidth, endY);
        }
    }

    public static int convertPixelsToDp(int px, Context context) {
        return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int convertDpToPixels(int pt, Context context) {
        return pt * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
