package com.xy.commonbase.utils;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.xy.commonbase.helper.LogHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class MediaUtil {

    // 缩放
    private  static  final  int PREVIEW_VIDEO_IMAGE_HEIGHT = 300; // Pixels



    public static File getVideoPreImage(String fileName) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        AssetFileDescriptor afd = null;
        Bitmap scaledBitmap = null;
        try {
//            afd = getAssets().openFd(fileName);
            mmr.setDataSource(fileName);
//            mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            Bitmap previewBitmap = mmr.getFrameAtTime();


            int videoWidth = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int videoHeight = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            int videoViewWidth = PREVIEW_VIDEO_IMAGE_HEIGHT * videoWidth / videoHeight;
            int videoViewHeight = PREVIEW_VIDEO_IMAGE_HEIGHT;
            scaledBitmap = Bitmap.createScaledBitmap(previewBitmap, videoViewWidth, videoViewHeight, true);

            mmr.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveFile(scaledBitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/aite", System.currentTimeMillis() + ".png");
    }

    public static File saveFile(Bitmap bm, String path, String fileName) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }
    public static String getRingDuring(String mUri){
        String duration=null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers=null;
                if (headers == null) {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        LogHelper.e("ryan   duration "+duration);
        return duration;
    }

}
