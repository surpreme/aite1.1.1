package com.xy.commonbase.web;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import com.xy.commonbase.R;

import java.io.File;


/**
 * Created by Dara on 2015/8/26 0026.
 */
public class ImagePick {

    private final int PICK_REQUEST = 0x1001;
    private final int TAKE_REQUEST = 0x1002;

    private static final String imgDir = Environment.getExternalStorageDirectory() + File.separator + "cloud";
    public static String imgFile = imgDir + File.separator + "tmp.jpg";

    private Activity activity;
    private Dialog dialog;

    public interface MyUri {
        void getUri(Uri uri);
    }

    public interface MyDismiss {
        void dismiss();
    }

    private MyDismiss md;

    public ImagePick(Activity activity) {
        this.activity = activity;

        dialog = new Dialog(activity, R.style.DialogStyle);
        View root = LayoutInflater.from(activity).inflate(R.layout.select_img, null);
        // 设置点击监听
        root.findViewById(R.id.take_photo).setOnClickListener((view -> takePhoto()));
        root.findViewById(R.id.album_photo).setOnClickListener(view -> getAlbum());
        root.findViewById(R.id.cancel).setOnClickListener(view -> {
            if (md!= null)
                md.dismiss();
            dialog.dismiss();
        });

        dialog.setContentView(root);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (md != null) {
                    md.dismiss();
                }
            }
        });
    }

    public void setCancel(MyDismiss md) {
        this.md = md;
    }

    /**
     * 显示Dialog
     */
    public void show() {
        dialog.show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        dialog.dismiss();
        new File(imgDir).mkdirs();
        File file = new File(imgFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imgUri = null;

        //        if (Build.VERSION.SDK_INT >= 24) {//这里用这种传统的方法无法调起相机
        //            imgUri = FileProvider.getUriForFile(mActivity, AUTHORITIES, imgFile);
        //        } else {
        //            imgUri = Uri.fromFile(imgFile);
        //        }
                /*
        * 1.现象
            在项目中调用相机拍照和录像的时候，android4.x,Android5.x,Android6.x均没有问题,在Android7.x下面直接闪退
           2.原因分析
            Android升级到7.0后对权限又做了一个更新即不允许出现以file://的形式调用隐式APP，需要用共享文件的形式：content:// URI
           3.解决方案
            下面是打开系统相机的方法，做了android各个版本的兼容:
        * */

        if (Build.VERSION.SDK_INT < 24) {
            // 从文件中创建uri
            imgUri = Uri.fromFile(file);
        } else {
            //兼容android7.0  使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            imgUri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        activity.startActivityForResult(intent, TAKE_REQUEST);
        /************/
//        String sdState = Environment.getExternalStorageState();
//        // 如果SD卡可读写
//        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
//            new File(imgDir).mkdirs();
//            File file = new File(imgFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//            activity.startActivityForResult(intent, TAKE_REQUEST);
//        } else {
//            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 从相册获取
     */
    private void getAlbum() {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, MyUri uri) {
        if (resultCode != activity.RESULT_OK) {
            uri.getUri(null);
            return;
        }
        if (requestCode == PICK_REQUEST) {
            if (data.getData() != null) {
                uri.getUri(data.getData());
            }else {
                uri.getUri(null);
            }
        } else if (requestCode == TAKE_REQUEST) {
            if (data != null) {
                Uri myUri;
                if (data.getData() != null) {
                    uri.getUri(data.getData());
                    return;
                }
                if (data.hasExtra("data")) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    try {
                        myUri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));
                        uri.getUri(myUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                        getUri(uri);
                    }
                } else {
                    // Nexus 6 返回Intent{}得不到数据
                    getUri(uri);
                }
            } else {
                getUri(uri);
            }
        }
    }

    private void getUri(MyUri uri) {
        File file = new File(imgFile);
        if (file != null && file.exists()) {
            uri.getUri(Uri.fromFile(file));
        }
    }

    /**
     * 裁剪图片，并按照给定的长宽输出
     *
     * @param activity
     * @param uri          拍照或者选择照片后获得的URI
     * @param outputWidth  输出的宽度
     * @param outputHeight 输出的高度
     * @param requestCode  请求码
     */
    public void cropImg(Activity activity, Uri uri, int outputWidth, int outputHeight, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", outputWidth);
        intent.putExtra("aspectY", outputHeight);
        intent.putExtra("outputX", outputWidth);
        intent.putExtra("outputY", outputHeight);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }
}