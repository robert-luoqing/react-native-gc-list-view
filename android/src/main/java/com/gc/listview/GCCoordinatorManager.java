package com.gc.listview;

import androidx.annotation.NonNull;
import android.view.View;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.image.ReactImageView;

//ReactViewManager
public class GCCoordinatorManager extends ViewGroupManager<GCCoordinatorView> {
  public static final String REACT_CLASS = "GCCoordinatorView";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @NonNull
  @Override
  protected GCCoordinatorView createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new GCCoordinatorView(reactContext);
  }

  @ReactProp(name = "forceIndex", defaultInt = 0)
  public void setForceIndex(GCCoordinatorView view, int forceIndex) {
    view.setForceIndex(forceIndex);
  }

  @ReactProp(name = "pixelRatio", defaultInt = -1)
  public void setPixelRatio(GCCoordinatorView view, float pixelRatio) {
    view.setPixelRatio(pixelRatio);
  }

  @Override
  public boolean needsCustomLayoutForChildren() {
    return true;
    // return super.needsCustomLayoutForChildren();
  }

  @ReactProp(name = "preloadFrame", defaultInt = 1)
  public void setPreloadFrame(GCCoordinatorView view, int preloadFrame) {
    view.setPreloadFrame(preloadFrame);
  }

  @ReactProp(name = "invert", defaultBoolean = false)
  public void setInvert(GCCoordinatorView view, boolean invert) {
    view.setInvert(invert);
  }

  @ReactProp(name = "data")
  public void setData(GCCoordinatorView view, ReadableArray data) {
    view.setData(data);
  }
}
