package com.GC;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @ReactProp(name = "itemLayouts")
    public void setItemLayouts(GCCoordinatorView view, ReadableArray itemLayouts) {
        view.setItemLayouts(itemLayouts);
    }

    @ReactProp(name = "categories")
    public void setCategories(GCCoordinatorView view, ReadableArray categories) {
        view.setCategories(categories);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
        // return super.needsCustomLayoutForChildren();
    }
}
