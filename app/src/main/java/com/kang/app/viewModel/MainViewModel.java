package com.kang.app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import common.library.LogPrint;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        LogPrint.println("----------ON_DESTROY");

    }
}
