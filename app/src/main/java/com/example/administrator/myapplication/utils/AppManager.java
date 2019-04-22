package com.example.administrator.myapplication.utils;

import android.app.Activity;
import java.util.Stack;

/**
 * APP的活动管理工具：用于activity的周期管理和应用程序的退出
 * 功能介绍        ： 使用栈来管理activity,打开activity将活动进栈，销毁activity将activity退栈。
 * 创建人          : Wanzhuang
 * 日期            : 2018.8.3
 * 参考资料         : https://blog.csdn.net/fzkf9225/article/details/73480469
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;


    private AppManager(){
    }

    /**
     * 单一实例
     *
     */
    public static AppManager getAppManager(){
        if(instance==null){
            instance= new AppManager();

        }
        return instance;
    }


    /**
     * 添加Activity到堆栈 */
    public void addActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) { }
    }


}
