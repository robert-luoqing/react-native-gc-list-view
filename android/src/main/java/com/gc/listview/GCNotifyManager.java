package com.gc.listview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

public class GCNotifyManager extends SimpleViewManager<GCNotifyView> {

  @NonNull
  @Override
  public String getName() {
    return "GCNotifyView";
  }

  @NonNull
  @Override
  protected GCNotifyView createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new GCNotifyView(reactContext);
  }

  @Nullable
  @Override
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.<String, Object>builder()
      .put(
        "onIndexChange",
        MapBuilder.of("registrationName", "onIndexChange"))
      .put(
        "onIndexFree",
        MapBuilder.of("registrationName", "onIndexFree"))
      .build();
  }
}
