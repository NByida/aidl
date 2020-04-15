## aidl demo 实现监听多进程app前后台切换事件

ApplicationActivityLifecycleCallbacks
```
  registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
 

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

       
    }
```
项目中webActivity使用了多进程，导致application重复创建多次
这里采用了binder方式实现多进程统计后台activity数目

