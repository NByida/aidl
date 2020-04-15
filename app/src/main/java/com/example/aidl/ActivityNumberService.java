package com.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yida.
 * @creat time 2020/4/15 10:34.
 */
public class ActivityNumberService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private AtomicInteger activityNub = new AtomicInteger(1);
    private RemoteCallbackList<ActivityNumChangeListenter> mListenerList = new RemoteCallbackList<>();

    private Binder binder = new ActivitynNumManger.Stub() {
        @Override
        public void addNum() throws RemoteException {
            activityNub.getAndAdd(1);
            int num = mListenerList.beginBroadcast();
            for (int i = 0; i < num; ++i) {
                ActivityNumChangeListenter listener = mListenerList.getBroadcastItem(i);
                Log.e("ActivityNumberService", "发送通知: " + listener.toString());
                listener.numChanged(activityNub.get());
            }
            mListenerList.finishBroadcast();
        }

        @Override
        public void removeNum() throws RemoteException {
            activityNub.decrementAndGet();
            int num = mListenerList.beginBroadcast();
            for (int i = 0; i < num; ++i) {
                ActivityNumChangeListenter listener = mListenerList.getBroadcastItem(i);
                Log.e("ActivityNumberService", "发送通知: " + listener.toString());
                listener.numChanged(activityNub.get());
            }
            mListenerList.finishBroadcast();
        }

        @Override
        public int getNum() throws RemoteException {
            return activityNub.get();
        }


        @Override
        public void register(ActivityNumChangeListenter listener) throws RemoteException {
            mListenerList.register(listener);
            int num = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e("ActivityNumberService", "添加完成, 注册接口数: " + num);
        }

        @Override
        public void ungister(ActivityNumChangeListenter listener) throws RemoteException {
            mListenerList.unregister(listener);
            int num = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e("ActivityNumberService", "删除完成, 注册接口数: " + num);
        }
    };

}
