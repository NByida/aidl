package com.example.aidl;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author yida.
 * @creat time 2020/4/15 13:56.
 */
public class MyApplication extends Application {
    static Application application;
    AppStatus appStatus;

    /**
     * 设置前后监听
     *
     * @param appStatus
     */
    public void setAppStatus(AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        startServices();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                try {
                    if (MyApplication.getManager() == null) return;
                    if (MyApplication.getManager().getNum() == -1) {
                        if (appStatus != null) {
                            appStatus.front();
                        }
                        Toast.makeText(MyApplication.application, "前台", Toast.LENGTH_SHORT).show();
                    }
                    MyApplication.getManager().addNum();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                try {
                    if (MyApplication.getManager() == null) return;
                    if (MyApplication.getManager().getNum() == 0) {
                        Toast.makeText(MyApplication.application, "后台", Toast.LENGTH_SHORT).show();
                        if (appStatus != null) {
                            appStatus.back();
                        }
                    }
                    MyApplication.getManager().removeNum();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private void startServices() {
        Intent intent = new Intent(this, ActivityNumberService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static ActivitynNumManger getManager() {
        return bookManager;
    }

    static ActivitynNumManger bookManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = ActivitynNumManger.Stub.asInterface(service);
            try {
                bookManager.register(new ActivityNumChangeListenter.Stub() {
                    @Override
                    public void numChanged(int num) throws RemoteException {
                        Log.e("xuyimin", "num" + num);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };


}
