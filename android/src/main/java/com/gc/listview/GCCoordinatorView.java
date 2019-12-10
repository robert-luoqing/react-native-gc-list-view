package com.gc.listview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.view.ReactViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GCCoordinatorView extends ReactViewGroup {
  private ReactScrollView scrollView;
  private ArrayList<GCNotifyView> notifyViews;

  private int forceIndex;
  private ArrayList<GCJSPassModel> decodeData;

  private int lastScrollViewY;

  /**
   * Content height
   */
  private int lastContentHeight = 0;

  private int lastCoordViewHeight = 0;

  /**
   * invert to show item
   */
  private boolean invert = false;

  public boolean isInvert() {
    return invert;
  }

  public void setInvert(boolean invert) {
    this.invert = invert;
    this.handleScroll(scrollView, this.lastScrollViewY, true);
  }

  /**
   * How much frame will be load
   */
  private int preloadFrame = 1;

  public int getPreloadFrame() {
    return preloadFrame;
  }

  public void setPreloadFrame(int preloadFrame) {
    this.preloadFrame = preloadFrame;
  }

  public static float pixelRatio;

  public void setPixelRatio(float ratio) {
    GCCoordinatorView.pixelRatio = ratio;
  }

  public int getForceIndex() {
    return forceIndex;
  }

  public void setForceIndex(int forceIndex) {
    this.forceIndex = forceIndex;
    this.handleScroll(scrollView, this.lastScrollViewY, true);
  }

  private ReadableArray data;

  public ReadableArray getData() {
    return data;
  }

  public void setData(ReadableArray data) {
    this.data = data;
    this.decodeData = new ArrayList<>();
    if (data != null) {
      for (int i = 0; i < data.size(); i++) {
        ReadableMap map = this.data.getMap(i);
        GCJSPassModel model = new GCJSPassModel(
          map.getInt("offset"),
          map.getString("key"),
          map.getString("category"));
        this.decodeData.add(model);
      }
    }

    this.lastContentHeight = 0;
    if (this.decodeData.size() > 0) {
      this.lastContentHeight = decodeData.get(decodeData.size() - 1).getOffset();
    }

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
        scrollView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
          @Override
          public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (bottom != oldBottom) {
              if (invert == true) {
                int gap = oldBottom - bottom;
                int scrollTo = lastScrollViewY + gap;
                if (scrollTo < 0) scrollTo = 0;
                scrollToY(scrollTo);
              }
            }
          }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
              GCCoordinatorView.this.handleScroll(scrollView, i1, false);
              Log.d("DEBUG", "coordinate: " + String.valueOf(i1));
            }
          });
        } else {
          scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
              int scrollY = scrollView.getScrollY(); //for verticalScrollView
              GCCoordinatorView.this.handleScroll(scrollView, scrollY, false);
              Log.d("DEBUG", "coordinate1: " + String.valueOf(scrollY));
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
        HashMap<String, GCNotifyView> assignedViews = new HashMap<String, GCNotifyView>();
        HashMap<String, ArrayList<GCNotifyView>> categoryViews = new HashMap<String, ArrayList<GCNotifyView>>();

        for (int notifyIndex = 0; notifyIndex < notifyCount; notifyIndex++) {
          GCNotifyView notifyObj = matchedNotifyViews.get(notifyIndex);
          if (notifyObj.getKey() != null && notifyObj.getKey() != "") {
            assignedViews.put(notifyObj.getKey(), notifyObj);
          } else {
            categoryNotifyView(categoryViews, notifyObj);
          }
        }

        ArrayList<GCNotifyVisiableModel> unhandledShowObjs = new ArrayList<>();
        for (int showIndex = 0; showIndex < showObjs.size(); showIndex++) {
          GCNotifyVisiableModel showObj = showObjs.get(showIndex);
          String key = showObj.getKey();
          if (key == null) key = "";
          GCNotifyView notifyObj = assignedViews.get(key);
          if (notifyObj != null) {
            notifyObj.notifyRebind(showObj, isForce);
            matchedNotifyViews.remove(notifyObj);
            assignedViews.remove(key);
          } else {
            unhandledShowObjs.add(showObj);
          }
        }

        // reput the assigned notify view to category
        for (GCNotifyView view : assignedViews.values()) {
          categoryNotifyView(categoryViews, view);
        }

        for (int i = 0; i < unhandledShowObjs.size(); i++) {
          GCNotifyVisiableModel showObj = unhandledShowObjs.get(i);
          if (matchedNotifyViews.size() > 0) {
            // Preority to get from category, if no category, the get from one which category is "";
            GCNotifyView notifyObj = null;
            String showObjCategory = showObj.getCategory();
            if (showObjCategory == null) {
              showObjCategory = "";
            }
            if (showObjCategory != "") {
              ArrayList<GCNotifyView> list = categoryViews.get(showObjCategory);
              if (list != null && list.size() > 0) {
                notifyObj = list.get(0);
              }
            }

            if (notifyObj == null) {
              ArrayList<GCNotifyView> list = categoryViews.get("");
              if (list != null && list.size() > 0) {
                notifyObj = list.get(0);
              } else {
                notifyObj = matchedNotifyViews.get(0);
              }
            }
            if (notifyObj != null) {
              String notifyObjCategory = notifyObj.getLastCategory();
              if (notifyObjCategory == null) {
                notifyObjCategory = "";
              }

              notifyObj.notifyRebind(showObj, isForce);
              matchedNotifyViews.remove(notifyObj);
              ArrayList clist = categoryViews.get(notifyObjCategory);
              if (clist != null && clist.contains(notifyObj)) {
                clist.remove(notifyObj);
              }
            }
          }
        }
      }

      for (int index = 0; index < matchedNotifyViews.size(); index++) {
        GCNotifyView notifyView = matchedNotifyViews.get(index);
        notifyView.emptyBind();
      }
    }
  }

  private void categoryNotifyView(HashMap<String, ArrayList<GCNotifyView>> categoryViews, GCNotifyView notifyObj) {
    // Category the view
    String category = notifyObj.getLastCategory();
    if (category == null) {
      category = "";
    }
    ArrayList<GCNotifyView> clist = categoryViews.get(category);
    if (clist == null) {
      clist = new ArrayList<>();
      categoryViews.put(category, clist);
    }
    clist.add(notifyObj);
  }

  public void handleScrollOutside() {
    this.handleScroll(scrollView, this.lastScrollViewY, true);
  }

  private void handleScroll(ScrollView scrollView, int y, boolean isForce) {
    this.lastScrollViewY = y;
    int scrollHeight = 0;
    if (scrollView != null) {
      scrollHeight = convertPixelsToDp(scrollView.getHeight(), this.getContext());
    }

    if (scrollHeight < 50) {
      DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
      int height = displayMetrics.heightPixels;
      scrollHeight = convertPixelsToDp(height, this.getContext());
    }

    handleScrollInner(scrollHeight, this.lastContentHeight, convertPixelsToDp(y, this.getContext()), isForce);
  }

  /**
   * @param scrollHeight the scrollHeight is dp unit
   * @param y            the y is dp unit
   * @param isForce
   */
  private void handleScrollInner(int scrollHeight, int contentHeight, int y, boolean isForce) {
    if (this.invert == true) {
      y = contentHeight - y;
    }

    int preLoadFrameHeight = this.getPreloadFrame() * scrollHeight;
    if (preLoadFrameHeight == 0) {
      preLoadFrameHeight = 50;
    }

    int minY = y - preLoadFrameHeight;
    if (minY < 0) {
      minY = 0;
    }

    int maxY = minY + scrollHeight + preLoadFrameHeight * 2;

    boolean isPassed = false;
    ArrayList<GCNotifyVisiableModel> showObjs = new ArrayList<>();
    int itemCount = 0;
    if (this.decodeData != null) {
      itemCount = this.decodeData.size();
    }

    for (int i = 0; i < itemCount; i++) {
      GCJSPassModel model = this.decodeData.get(i);
      int startY = 0;
      int endY = model.getOffset();
      if (i != 0) {
        GCJSPassModel prevModel = this.decodeData.get(i - 1);
        startY = prevModel.getOffset();
      }

      // It mean the element fall in visual area
      if ((startY <= minY && endY >= minY)
        || (startY >= minY && endY <= maxY)
        || (startY <= maxY && endY >= maxY)
      ) {
        String category = model.getCategory();
        if (category == null) category = "";

        GCNotifyVisiableModel notifyVisiableModel = null;
        if (this.invert == true) {
          notifyVisiableModel = new GCNotifyVisiableModel(i, model.getKey(), contentHeight - endY, contentHeight - startY, category);
        } else {
          notifyVisiableModel = new GCNotifyVisiableModel(i, model.getKey(), startY, endY, category);
        }

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
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

  }

  private void scrollToY(int tempScrollTo) {
    final int scrollTo = tempScrollTo;
    Handler mHandler = new Handler();
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        scrollView.scrollTo(0, scrollTo);
      }
    });
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    this.relayoutChildren();
    // Handle size changed
    int newHeight = (bottom - top);
    if(this.lastCoordViewHeight != newHeight) {
      this.handleScrollOutside();
      if (this.invert) {
        int gap = this.lastCoordViewHeight - (this.lastScrollViewY + this.scrollView.getHeight());
        int tempScrollTo = 0;
        if (gap < 50) {
          tempScrollTo = newHeight - gap - this.scrollView.getHeight();
          if (tempScrollTo < 0) tempScrollTo = 0;
          scrollToY(tempScrollTo);
        }
      }
    }

    this.lastCoordViewHeight = newHeight;
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
    if (GCCoordinatorView.pixelRatio >= 1) {
      return Math.round(px / GCCoordinatorView.pixelRatio);
    } else {
      return Math.round(px / ((float) context.getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT));
    }
  }

  public static int convertDpToPixels(int pt, Context context) {
    if (GCCoordinatorView.pixelRatio >= 1) {
      return Math.round(pt * GCCoordinatorView.pixelRatio);
    } else {
      return Math.round(pt * ((float) context.getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT));
    }
  }
}
