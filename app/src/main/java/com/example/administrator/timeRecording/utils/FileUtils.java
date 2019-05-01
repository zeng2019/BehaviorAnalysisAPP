package com.example.administrator.timeRecording.utils;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 名称     ：fileUtils
 * 主要内容 ：工具类,用于创建文件，将扫描到的数据写到文本里，（不完善待修改）
 * 创建人   ：wanzhuang
 * 创建时间： 2018.6.
 */
public class FileUtils {

    //
    private Context mContext;
    private static FileUtils instance;
    //
    private FileUtils(){

    }
    public FileUtils(Context context){
        super();
        this.mContext = context;
    }
    /**
     *单一实例
     */
    public static FileUtils getInstance(){
        if(instance==null){
            instance = new FileUtils();
        }
        return instance;
    }

    /**
     * 写文本
     */
    public void writeText(String filename,String filecontent){
        //创建文本输出流
        try{
            //MODE.PRIVE 覆盖创建,MODE_APPEND 在已存在的文档中追加
            FileOutputStream output= mContext.openFileOutput(filename,Context.MODE_APPEND);
            //将字符串以字节流的形式写入到输出流中
            output.write(filecontent.getBytes());
            //关闭输出流
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 读取文本
     */
    public String  readText(String filename){
        String readStr="";

        try{
            FileInputStream input=new FileInputStream(filename);
            byte[] temp=new byte[input.available()];
            readStr= new String(temp);
            input.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return readStr;
    }
}
