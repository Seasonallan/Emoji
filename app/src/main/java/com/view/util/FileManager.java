package com.view.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.season.emoji.ui.BaseApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FileManager {
    public static Context context;
    public static String videoPath = Environment.getExternalStorageDirectory().getPath();

    public FileManager() {
    }

    //得到内部缓存路径
    public static File getCacheFileParent(String type) {
        File file = new File(BaseApplication.getInstance().getCacheDir(), type);
        return file;
    }

    public static File getBitmapFromStickerLayeroutputFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                .FileManager.BQ_Separator), Constant.FileManager.Bitmap);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Merge_VID_" + timeStamp + ".mp4");
        } else if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "GIf_" + timeStamp + ".gif");
        } else {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Bitmap_" + timeStamp + ".png");
        }

        return mediaFile;
    }

    /***********************************************制作界面相关 end**************************************************/

    /***********************************************
     * 拍摄界面相关 begin
     **************************************************/
    public static File getFile4Share(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                .FileManager.BQ_Separator), Constant.FileManager.Share);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = null;
        switch (type) {
            case 1:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "ShareTextLayer.png");
                break;
            case 2:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Share.mp4");
                break;
            case 3:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AdjustResolution.mp4");
                break;
            case 4:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AdjustResolution.png");
                break;
            case 5:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "sharegif.gif");
                break;
        }
        return mediaFile;
    }

    /**
     * 与Constant类的CameraFileNameTag对应
     * <p>
     * String NORMAL_VIDEO = "normalvideo.mp4";
     * String REVERSE = "reverse.mp4";
     * String PINGPANG ="pingpang.mp4";
     * String CROP_VIDEO_SIZE ="crop.mp4";
     * String RESOLUTION_VIDEO ="resolution.mp4";
     * String MERGE_VIDEO ="merge.mp4";
     * String OVERLAY ="overlay.png";
     * String OVERLAY4VIDEO ="overlay4video.png";
     * String FINALGIF ="final.gif";
     * String NORMAL_PHOTO ="normalphoto.png";
     * String MERGE_PHOTO ="mergephoto.png";
     * String FINALPHOTO ="finalphoto.png";
     */
    public static String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        return timeStamp;
    }

    /**
     * 如果发送失败就保存在草稿
     * https://img.biaoqing.com/AndroidTest1502097832242_0 原测试命名格式
     * androidTest/work/20170718/10263301148.gif 测试命名格式
     * work/20170718/10263301148.gif 正式命名格式
     */
    public static String getCurrentTime4Publish() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd/HHmmss", Locale.CHINESE).format(new Date());
        return timeStamp;
    }

    /**
     * private String savePath = "/uploads/{year}{mon}{day}/{random32}{.suffix}";
     * 类型	格式	说明
     * 绝对值	String	指定具体的路径，如: /path/to/file.txt
     * 时间类	{year} {mon} {day} {hour} {min} {sec}	日期、时间相关内容（UTC 时间）
     * md5 类	{filemd5}	文件的 md5 值
     * 随机类	{random} {random32}	16 位或 32 位随机字符和数字
     * 文件名	{filename} {suffix} {.suffix}	上传文件的文件名及扩展名
     */
    public static String getCurrentTime4PublishUpyun() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd/HHmmss", Locale.CHINESE).format(new Date());
        return timeStamp;
    }

    public static File getCameraFileName(int type) {
        File mediaStorageDir;
        if (GlobalStatus.isDebug) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                    .FileManager.BQ_Separator), Constant.FileManager.Camera);
        } else {
            mediaStorageDir = getCacheFileParent(Constant.FileManager.Camera);
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile = null;
        switch (type) {
            case 1:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.NORMAL_VIDEO);
                break;
            case 2:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.REVERSE);
                break;
            case 3:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.PINGPANG);
                break;
            case 4:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.CROP_VIDEO_SIZE);
                break;
            case 5:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.RESOLUTION_VIDEO);
                break;
            case 6:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.MERGE_VIDEO);
                break;
            case 7:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.OVERLAY);
                break;
            case 8:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.OVERLAY4VIDEO);
                break;
            case 9:
                File mediaStorageDir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +
                        Constant.FileManager.BQ_Separator), Constant.FileManager.Share);
                if (!mediaStorageDir2.exists()) {
                    if (!mediaStorageDir2.mkdirs()) {
                        return null;
                    }
                }
                mediaFile = new File(mediaStorageDir2.getPath() + File.separator + getCurrentTime() + Constant.CameraFileName
                        .FINALGIF);
                break;
            case 10:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.NORMAL_PHOTO);
                break;
            case 11:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.MERGE_PHOTO);
                break;
            case 12:
                File mediaStorageDir3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +
                        Constant.FileManager.BQ_Separator), Constant.FileManager.Share);
                if (!mediaStorageDir3.exists()) {
                    if (!mediaStorageDir3.mkdirs()) {
                        return null;
                    }
                }
                mediaFile = new File(mediaStorageDir3.getPath() + File.separator + getCurrentTime() + Constant.CameraFileName
                        .FINALPHOTO);
                break;
            case 13:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.CROP_VIDEO_DURATION);
                break;
            case 14:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.SPEED_VIDEO);
                break;
            case 15:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.GIF_COLOUR_BOARD);
                break;
            case 16:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.IMG_WATERMARK_IOS_ORIGINAL);
                break;
            case 17:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.IMG_WATERMARK_IOS_ADJUST);
                break;
            case 18:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.NORMAL_VIDEO_PLUS_KEY_FRAME);
                break;
            case 19:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.POSITIVE_TS);
                break;
            case 20:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.CameraFileName.REVERSE_TS);
                break;
        }
        return mediaFile;
    }

    public static File getCameraFileName4Split(int num) {
        File mediaStorageDir;
        if (GlobalStatus.isDebug) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                    .FileManager.BQ_Separator), Constant.FileManager.Camera);
        } else {
            mediaStorageDir = getCacheFileParent(Constant.FileManager.Camera);
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "normalvideo" + num + ".mp4");
        return mediaFile;
    }

    private static File getDiyDir() {
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
        // .FileManager.BQ_Separator), "DIY");
        File mediaStorageDir = new File(BaseApplication.getInstance().getCacheDir(), Constant.FileManager.DIY);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }

    public static File getDiyLayerDir() {
        File parentFile = getDiyDir();
        if (parentFile == null) {
            return null;
        }
        File fileDir = new File(parentFile, Constant.FileManager.Layer);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return null;
            }
        }
        return fileDir;
    }

    public static File getDiyBackgroundFile(String name, String type) {
        return getDiyFile(Constant.FileManager.Background, name, type);
    }

    public static File getDiyDownloadFile(String name, String type) {
        return getDiyFile(Constant.FileManager.Download, name, type);
    }

    public static File getDiyCropFile() {
        return getDiyFile(Constant.FileManager.Crop, null, "png");
    }

    public static File getDiyMaterialFile(String name, String type) {
        return getDiyFile(Constant.FileManager.Material, name, type);
    }

    public static File getDiyLayerFile(String name, String type) {
        return getDiyFile(Constant.FileManager.Layer, name, type);
    }

    public static File getDiyFontFile(String name) {
        return getDiyFile(Constant.FileManager.Font, name, "ttf");
    }

    public static File getDiyShareFile(String name, String type) {
        return getDiyFile(Constant.FileManager.Share, name, type);
    }

    public static File getCameraBitmapFileDir() {
        File mediaStorageDir;
        if (GlobalStatus.isDebug) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ Constant.FileManager.BQ_Separator), Constant
                    .FileManager.CAMERA_BITMAP);
        } else {
            mediaStorageDir = new File(BaseApplication.getInstance().getCacheDir(), Constant.FileManager.CAMERA_BITMAP);
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }

    private static File getDiyFile(String dir, String name, String type) {
        File parentFile = getDiyDir();
        if (parentFile == null) {
            return null;
        }
        File fileDir = new File(parentFile, dir);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return null;
            }
        }
        if (TextUtils.isEmpty(name)) {
            name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        }
        return new File(fileDir, name + "." + type);
    }

    public static File getCameraSaveLocalAlbumFile(int tag) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constant
                .FileManager.BQ);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = null;
        if (tag == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else if (tag == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else if (tag == 3) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "GIF_" + timeStamp + ".gif");
        }
        return mediaFile;
    }

    public static File getImageSaveLocalAlbumFile(String type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constant
                .FileManager.BQ);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + type);

        return mediaFile;
    }

    public static File getShareLocalFile(String type) {
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant.FileManager
                .BQ_Separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + type);
    }

    public static File getDiyFile(String type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                .FileManager.BQ_Separator), Constant.FileManager.DIY);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + type);
    }

    public static File getShareFile(String type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/表情说说"),
                "Share");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + type);
    }

    /**
     * 下载webp原图到缓存路径
     *
     * @param type
     * @return
     */
    public static File getCacheFile(String type) {
        File mediaStorageDir = BaseApplication.getInstance().getExternalCacheDir();
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + type);

        return mediaFile;
    }

    public static File getVideoThumbsFile(Context m) {
        File mediaStorageDir;
        if (GlobalStatus.isDebug) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                    .FileManager.BQ_Separator), Constant.FileManager.Thumbs);
        } else {
            //改成内部存储
            mediaStorageDir = new File(BaseApplication.getInstance().getCacheDir(), Constant.FileManager.Thumbs);
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
//        File file = new File(mediaStorageDir.getPath() + File.separator + name + ".ttf");
        //内部存储
//        if (m == null) {
//            return null;
//        }
//        File file = new File(m.getFilesDir(), name + ".ttf");
        return mediaStorageDir;
    }

    /**
     * 这里是仅供app使用，不暴露给其他App和用户
     *
     * @return
     */
    public static File getApkDownloadFile(Context m, String name) {
        //改成内部存储
        //        File file = new File(m.getFilesDir(), name + ".ttf");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                .FileManager.BQ_Separator), Constant.FileManager.APK);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + name + ".apk");
        return file;
    }

    /***********************************************
     * 拍摄界面相关 end
     **************************************************/
    //发布的图片，分辨率大于720，进行了压缩保存
    public static File getFile4PublishCompress(int type) {
        //这里将用于分享，必须放在外部
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Constant
                .FileManager.BQ_Separator), Constant.FileManager.Compress);
//        File mediaStorageDir = new File(BaseApplication.getInstance().getCacheDir(), Constant.FileManager.Compress);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = null;
        switch (type) {
            case 1:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + "compress.png");
                break;
            case 2:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + "compress.jpg");
                break;
        }
        return mediaFile;
    }

}
