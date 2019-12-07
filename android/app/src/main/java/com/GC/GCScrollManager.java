package com.GC;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.scroll.ReactScrollViewManager;

public class GCScrollManager extends ReactScrollViewManager {
    public static final String REACT_CLASS = "GCCustomScrollView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ReactScrollView createViewInstance(ThemedReactContext context) {
        return new GCScrollView(context);
    }
}
