package com.example.administrator.timeRecording.utils;


import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * MD5utils : 消息摘要算法,用于字符串和文件的一致性检验
 * 创建人    ：Wanzhuang
 * 日期      ：2018.8.5
 *
 */
public class MD5utils {

    private static MD5utils instance;

    private MD5utils(){
    }

    /**
     * 单一实例
     * @return
     */
    public static MD5utils getInstance(){
        if(instance==null){
            instance =new MD5utils();
        }
        return instance;
    }
    /*
     * 获取字符串的 MD5
     */
    public static String MD5(String string){
        if(TextUtils.isEmpty(string)){
            return "";

        }
        MessageDigest md5 = null;
        try{
            // 获得MD5摘要算法的 MessageDigest 对象
            md5 = MessageDigest.getInstance("MD5");
            // 使用指定字节进行更新
            md5.update(string.getBytes());
            // 获得密文
            byte[] bytes =md5.digest();
            StringBuilder hexString =new StringBuilder();
            for (byte b:bytes){
                 hexString.append(String.format("%02X",b));
            }
            return hexString.toString().toLowerCase();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }
     /*
      *获取文件的 MD5
      */
    public static String MD5(File file){
        if(file ==null||!file.isFile()||!file.exists()){
            return "";
        }
        try{
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md5=MessageDigest.getInstance("MD5");
            // 文件输入流
            FileInputStream inputStream = new FileInputStream(file);
            DigestInputStream digestInputStream = new DigestInputStream(inputStream,md5);
            //必须把文件读取完毕才能拿到md5
            byte[] buffer = new byte[4096];
            while(digestInputStream.read(buffer)>-1){}
            //获取最终的MessageDigest
            MessageDigest digest =digestInputStream.getMessageDigest();
            digestInputStream.close();
            inputStream.close();
            // 获得密文
            byte[] bytes=digest.digest();
            StringBuilder stringBuilder=new StringBuilder();
            for (byte b: bytes){
                stringBuilder.append(String.format("%02x",b));
            }
            return stringBuilder.toString().toLowerCase();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
