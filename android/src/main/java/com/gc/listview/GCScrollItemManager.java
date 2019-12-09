package com.gc.listview;


import androidx.annotation.NonNull;
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
}