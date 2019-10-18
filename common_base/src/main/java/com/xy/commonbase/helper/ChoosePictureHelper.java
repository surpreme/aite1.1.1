package com.xy.commonbase.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.xy.commonbase.utils.FileUtil;

import java.io.File;
import java.lang.ref.WeakReference;

public class ChoosePictureHelper {
    // 请求打开相册
    public static final int RC_CHOOSE_PHOTO = 50001;
    // 请求打开相机
    public static final int RC_TAKE_PHOTO = 50002;

    private WeakReference<Activity> reference;

    public ChoosePictureHelper(Activity activity) {
        reference = new WeakReference<>(activity);
    }

    public void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        reference.get().startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }


    public File takePhoto() {
        // 步骤一：创建存储照片的文件
        Uri mUri;
        String path = FileUtil.getFilesDir() + File.separator + "moheadline" + File.separator;
        File photoFile = new File(path, System.currentTimeMillis() / 1000 + "_myPicture.jpg");
        if (!photoFile.getParentFile().exists())
            photoFile.getParentFile().mkdirs();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(reference.get(), reference.get().getPackageName() + ".fileprovider", photoFile);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(photoFile);
        }
        //步骤四：调取系统拍照
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        reference.get().startActivityForResult(intent, RC_TAKE_PHOTO);
        return photoFile;
    }
}
