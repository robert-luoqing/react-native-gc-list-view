package com.GC;


import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class GCScrollItemManager extends ViewGroupManager<GCScrollItemView> {
    public static final String REACT_CLASS = "GCScrollItemContainerView";
    // register self to mock to update properties
    private static GCScrollItemManager instance = null;

    public GCScrollItemManager() {
        instance = this;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected GCScrollItemView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new GCScrollItemView(reactContext);
    }

    public static void updateLayout(GCScrollItemView view, int y, int height) {
//        WritableNativeMap readableMap = new WritableNativeMap();
//        readableMap.putInt("top", y);
//        readableMap.putInt("left", 0);
//        readableMap.putInt("right", 0);
//        readableMap.putString("position", "absolute");
//        readableMap.putInt("height", height);
//        ReactStylesDiffMap map = new ReactStylesDiffMap(readableMap);
//        instance.updateProperties(view, map);
//        view.requestLayout();
    }
}