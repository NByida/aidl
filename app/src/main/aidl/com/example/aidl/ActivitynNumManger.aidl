// ActivitynNumManger.aidl
package com.example.aidl;
import  com.example.aidl.ActivityNumChangeListenter;
// Declare any non-default types here with import statements

interface ActivitynNumManger {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

      void addNum();
      void removeNum();
      int getNum();
      void register(ActivityNumChangeListenter  listener);
      void ungister(ActivityNumChangeListenter  listener);
}
