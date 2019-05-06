package com.example.administrator.timeRecording.UI;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.timeRecording.BaseActivity;
import com.example.administrator.timeRecording.R;
import com.example.administrator.timeRecording.utils.Glide4Engine;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 名称     ：upImage
 * 主要内容  ：用于上传头像（待修改）目前只能上选择，不能剪裁等
 * 创建人   ：wanzhuang
 * 创建时间 ：2018.8.10
 *
 */

public class upImage extends BaseActivity {
    ImageView imageView;
    Button button1;
    Button button2;
    List<LocalMedia>localMedia =new ArrayList<>();
    List<Uri> list=new ArrayList<>();
    Uri uri;
    private static final int REQUEST_CODE_CHOOSE=23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_image);
        init();

    }
    private void init(){
        imageView=(ImageView)findViewById(R.id.imageView);
        button1=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);

        RxPermissions rxPermissions=new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                     Matisse.from(upImage.this)
                             .choose(MimeType.ofAll(),false)//kinds of pic
                             .countable(true) //
                             .capture(true)// camera
                             .captureStrategy(
                                     new CaptureStrategy(true,"com.example.administrator.myapplication.fileprovider"))
                             .maxSelectable(1)
                             .thumbnailScale(0.85f)
                             .imageEngine(new Glide4Engine())//加载方式
                             .originalEnable(true)
                             .maxOriginalSize(10)
                             .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                             .forResult(REQUEST_CODE_CHOOSE);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matisse.from(upImage.this)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(true,"com.example.administrator.myapplication.fileprovider"))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())

                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .forResult(REQUEST_CODE_CHOOSE);


            }
        });
        //Pick
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // OkGo.<File>post(Urls.url_photo)
               //         .tag(this)
                //        .op
                PictureSelector.create(upImage.this)
                        .openGallery(PictureMimeType.ofAll())
                        .maxSelectNum(1)
                        .imageSpanCount(3)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .isCamera(true)//
                        .isZoomAnim(true)
                        .sizeMultiplier(0.85f)
                        .enableCrop(true)
                        //.compress(true)
                        // .compressSavePath(getPath())//压缩图片保存地址
                        .withAspectRatio(1,1)
                        .freeStyleCropEnabled(true)
                        .showCropGrid(true)
                        .openClickSound(false)// 是否开启点击声音 true or false
                        .selectionMedia(localMedia)// 是否传入已选图片 List<LocalMedia> list
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                        //.minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.cropWH(1,1)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        //.videoQuality()// 视频录制质量 0 or 1 int
                        // .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                        //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                        // .recordVideoSecond()//视频秒数录制 默认60s int
                        .isDragFrame(false)// 是否可拖动裁剪框(固定)
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    //有错误
                    localMedia=PictureSelector.obtainMultipleResult(data);
                    for(LocalMedia media:localMedia){
                    Bitmap bitmap= BitmapFactory.decodeFile(media.getPath());
                       imageView.setImageBitmap(bitmap);
                    }
                    break;
                case REQUEST_CODE_CHOOSE:
                    list=Matisse.obtainResult(data);
                    uri =list.get(0);
                    imageView.setImageURI(uri);

                    break;
            }
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(upImage.this);
    }
}
