package com.view.util;

/**
 * Created by Administrator on 2017/10/20.
 */

public class GlobalStatus {
    public static boolean isVideo;
    public static boolean isFromAlbum;//来自相册
    public static boolean isGif;
    //制作部分完成的时候，设置为true，将开启改图按钮，和首页制作按钮
    public static boolean isDiyDone = true;
    //字体动效完成的时候，设置为true，将开启动效按钮
    public static boolean isTextAnimationDone = true;
    //默认false, 当发布完成后置为true，用于DiyMainActivity中onDestroy判断是否需要保存图层信息到本地
    public static boolean isDiyPublished = false;
    //提示讯飞提供技术支持
    public static boolean isShowXunFeiSupportTips = true;
    //发布的时候必须设置为false;
    public static boolean isDebug = false;//影响发布的文件命名,开为Debug,友盟Debug,拍摄生成文件和视频缩率图在debug下在外存，release在内部存储
    public static String TOPIC = "";//如果是话题页面，进行拍摄，或者制作而去发布，把相应的话题，带到发布页面。
}
