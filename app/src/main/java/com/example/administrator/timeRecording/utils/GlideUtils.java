package com.example.administrator.timeRecording.utils;

import android.content.Context;

import android.net.Uri;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.io.File;

/**
 * GlideUtils :Glide封装类,实现加载图片,gif,视频。
 * 创建人      ：wanzhuang
 * 创建时间    ：2018.8.20
 *
 */

public class GlideUtils {

    private static GlideUtils minstance;
    private GlideUtils(){
    }

    public static GlideUtils getInstance(){
        if(minstance==null){
            synchronized (GlideUtils.class){
                if(minstance==null){
                    minstance=new GlideUtils();
                }
            }
        }
        return minstance;
    }
    /*
     * option
     */
    static class Constant{
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 20; //圆角
        public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小

    }

    /**
     *
     */
    private static RequestOptions options=new RequestOptions()
            // .error()//错误占位符
            // .placeholder()//加载占位符
            // .override(500,500)//设置图片大小
            // .skipMemoryCache(true)//
            // .dontAnimate()//直接显示图片，取消淡入淡出效果
            // .crossFade()//
            // .centerCrop()//即缩放图像让它填充到 ImageView 界限内并且裁剪额外的部分
            // .Fitcenter()//即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。
            //               该图像将会完全显示，但可能不会填满整个 ImageView。
            // .diskCacheStrategy()//DiskCacheStrategy.NONE 什么都不缓存，就像刚讨论的那样
                                   //DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。
                                   //  DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
                                   //  DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
            //.priority() //Priority.LOW
                        // Priority.NORMAL
                        //Priority.HIGH
                        //Priority.IMMEDIATE

    ;

    /**
     * 默认加载URL路径的图片
     * @param context
     * @param url
     * @param view
     */

    public static void imageLoader(Context context,String url,ImageView view){
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(view);

    }
    //resoureid
    public static void imageLoader(Context context,int id,ImageView view){
        Glide.with(context)
                .load(id)
                .into(view);
    }
    //uri
    public static void imageLoader(Context context, Uri uri,ImageView view){
        Glide.with(context)
                .load(uri)
               // .error(R.drawable.error)
                .into(view);
    }
    //file
    public static void imageLoader(Context context,File file,ImageView view,RequestOptions
                                   option){
        Glide.with(context)
                .load(file)
                .apply(option)
                .into(view);
    }
    //gif
    public static void gifLoader(Context context,String gifurl,ImageView view) {
        Glide.with(context)
                .load(gifurl)

                .into(view);

        //img Targe

    }

}

