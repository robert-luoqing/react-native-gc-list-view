package com.v21test4;

import androidx.annotation.NonNull;

import com.GC.GCCoordinatorManager;
import com.GC.GCScrollItemManager;
import com.GC.GCScrollManager;
import com.GC.GCNotifyManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CGPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(
            ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new GCScrollItemManager(),
                new GCScrollManager(),
                new GCCoordinatorManager(),
                new GCNotifyManager()
        );
    }
}
