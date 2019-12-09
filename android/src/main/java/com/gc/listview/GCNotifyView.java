package com.gc.listview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.scroll.ReactScrollView;

public class GCNotifyView extends View {
  private ReactScrollView scrollView;
  private GCScrollItemView parentView;
  private int index = -1;
  private long lastBind;
  // Priority to use same category element
  private String lastCategory;
  /**
   * Some gif may cost js and ui thread.
   * If free the item. these item should need remove gif from UI
   */
  private boolean released = false;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getLastCategory() {
    return lastCategory;
  }

  public void setLastCategory(String lastCategory) {
    this.lastCategory = lastCategory;
  }

  public GCNotifyView(Context context) {
    super(context);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (scrollView == null) {
      scrollView = GCCoordinatorView.fintSpecifyParent(ReactScrollView.class, this.getParent());
      if (scrollView != null) {
        GCCoordinatorView coordinatorView = GCCoordinatorView.fintSpecifyParent(GCCoordinatorView.class, this.getParent());
        coordinatorView.registerView(this);
        coordinatorView.handleScrollOutside();
      }
    }
    if (parentView == null) {
      parentView = GCCoordinatorView.fintSpecifyParent(GCScrollItemView.class, this.getParent());
    }
  }

  private void notifyIndexChanged(int index, int startY, int endY) {
    WritableMap event = Arguments.createMap();
    event.putInt("index", index);
    event.putInt("startY", startY);
    event.putInt("endY", endY);
    ReactContext reactContext = (ReactContext) getContext();
    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
      getId(),
      "onIndexChange",
      event);
  }

  private void notifyIndexToFree() {
    WritableMap event = Arguments.createMap();
    ReactContext reactContext = (ReactContext) getContext();
    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
      getId(),
      "onIndexFree",
      event);
  }

  public void notifyRebind(GCNotifyVisiableModel model, boolean isForce) {
    int oldIndex = this.index;
    long now = System.currentTimeMillis();
    this.index = model.getIndex();
    this.lastCategory = model.getCategory();
    this.released = false;
    // The rect's y and height
    int elementY = 0;
    int elementHeight = 0;

    if (this.parentView != null) {
      if (this.parentView.getStartY() != model.getStartY()
        || this.parentView.getEndY() != model.getEndY()) {
        this.parentView.setStartY(model.getStartY());
        this.parentView.setEndY(model.getEndY());
        GCCoordinatorView coordinatorView = GCCoordinatorView.fintSpecifyParent(GCCoordinatorView.class, this.getParent());
        if (coordinatorView != null) {
          coordinatorView.relayoutChildren();
        }
      }
    }

    if (isForce) {
      this.notifyIndexChanged(model.getIndex(), model.getStartY(), model.getEndY());
    } else {
      if (oldIndex != this.index
        || elementY != model.getStartY()
        || elementHeight != (model.getEndY() - model.getStartY())
      ) {
        this.notifyIndexChanged(model.getIndex(), model.getStartY(), model.getEndY());
      } else {
        if ((now - lastBind) > 200) {
          this.notifyIndexChanged(model.getIndex(), model.getStartY(), model.getEndY());
        }
      }
    }

    this.lastBind = now;
  }

  public void emptyBind() {
    //  self.index = -1;
    if (this.parentView != null) {
      if (this.parentView.getStartY() != -100000) {
        this.parentView.setStartY(-100000);
        GCCoordinatorView coordinatorView = GCCoordinatorView.fintSpecifyParent(GCCoordinatorView.class, this.getParent());
        if (coordinatorView != null) {
          coordinatorView.relayoutChildren();
        }
      }
    }

    if(this.released == false) {
      this.released = true;
      this.notifyIndexToFree();
    }
  }
}
