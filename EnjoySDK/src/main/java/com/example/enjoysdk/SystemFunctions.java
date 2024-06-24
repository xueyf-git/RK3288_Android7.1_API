package com.example.enjoysdk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SystemFunctions {
    private final Activity mActivity;
    public SystemFunctions(Activity activity) { this.mActivity = activity; }

    /**
     * 截图
     * @param 无
     * @return 成功返回 ENJOY_COMMON_SUCCESSFUL
     */
    public int takeScreenshot() {
        View rootView = mActivity.getWindow().getDecorView().getRootView();

        Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);

        try {
            // 设置保存路径
            String path = Environment.getExternalStorageDirectory().getPath() + "/Screenshots/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 生成唯一的文件名
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "Screenshot_" + timeStamp + ".png";
            String filePath = path + fileName;

            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            // 通知系统媒体库更新
            MediaScannerConnection.scanFile(mActivity, new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("Screenshot", "Scanned " + path + ":");
                    Log.i("Screenshot", "-> uri=" + uri);
                }
            });

            // 发送广播通知媒体库更新
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(filePath);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            mActivity.sendBroadcast(mediaScanIntent);

            Toast.makeText(mActivity, "截图保存成功", Toast.LENGTH_SHORT).show();
            return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_SUCCESSFUL;
        } catch (Exception e) {
            Log.e("ScreenshotError", "Error in taking screenshot: " + e.getMessage());
            Toast.makeText(mActivity, "截图保存失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return com.example.enjoysdk.EnjoyErrorCode.ENJOY_COMMON_ERROR_UNKNOWN;
        }
    }
}
