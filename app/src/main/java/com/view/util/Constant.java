package com.view.util;

import com.zhy.autolayout.utils.AutoUtils;

public interface Constant {
//    boolean isDebug = false;//影响发布的文件命名,开为Debug,友盟Debug
    int LOW_DEVICE_MEMORY_VALUE = 200;
    long SD_SPACE_LIMIT = 1024*1024*50;//低于50m 就认为体积不够
    int ALBUM_REQUEST = 1;// 请求图库RequestCode
    int TAKE_PHOTO_REQUEST = 2;// 拍照RequestCode
    String HOME_CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";
    String PREFS_COMMON = "pref_common";  //通用SP保存文件
    int CURRINDEX = 10001;
    String LOGIN_DEVICE = "Android";
    String ALIAS_TYPE = "user_id";//推送alias别名类型设置，统一为自有id，自己设置
    String INDEX_PAGER_TYPE = "index_pager_type";
    String STICKER_PAGER_TYPE = "sticker_pager_type";
    String STICKER_DISTORTION_IS_NOT = "sticker_distortion_is_not";
    String SUBJECT_DETAIL_PAGE_ID = "subject_detail_page_id";
    String SUBJECT_DETAIL_PAGE_KEYWORD = "subject_detail_page_keyword";
    String SUBJECT_DETAIL_PAGE_TYPE = "subject_detail_page_type";
    String SUBJECT_DETAIL_NEED_FINISH = "subject_detail_need_finish";
    int StickerMaxNumPerPage = 200;//贴纸单页面最多贴纸数量

    String SHOW_GRADE_DIALOG = "show_grade_dialog";

    //便于清空缓存,用于分享的文件必须放在外存
    interface FileManager {
        String BQ = "表情说说";
        String BQ_Separator = "/表情说说";
        String Bitmap = "Bitmap";
        String APK = "Apk";
        String Share = "Share";//发布的时候放在外部，将可能用于分享
        String Camera = "Camera";//发布的时候放在内部
        String Thumbs = "Thumbs";//发布的时候放在内部
        String Compress = "Compress";//发布的时候放在外部，将可能用于分享
        String DIY = "DIY";
        String CAMERA_BITMAP = "VideoBitmap";//视频生成的bitmap文件缓存目录
        //以下都是DIY的二级目录
        String Layer = "Layer";
        String Download = "Download";
        String Crop = "Crop";
        String Material = "Material";
        String Font = "Font";
        String Background = "Background";

    }

    interface OpenParams {
        //微信
        String APPID_WEIXIN = "wxc14643be5ab20487";
        String APPKEY_WEIXIN = "89a4b6a40c51de8e48685f3360bf9ce1";
        //要去QQ互联下获取
        String APPID_QQ = "1105677233";
        String APPKEY_QQ = "DspGOsreiWxcoYtZ";
        //微博
        String APPID_WEIBO = "1291997798";
        String APPKEY_WEIBO = "3a33c468d8c540ccff0e6883297c8f09";
        String REDIRECTURL_WEIBO = "http://sns.whalecloud.com";
    }

    //七牛云图片压缩裁剪
    interface ImageStr {
        String IndexMagicStr = "-thumb";
        String smallMagicStr = "-thumb";
        String avatarMagicStr = "";
    }

    //用户保存数据
    interface UserInfo {
        String PREFS_USER_INFO = "USER_INFO_PREFS";
        String USERID = "userId";
        String USERTOKEN = "userToken";
        String USERAVATAR = "userAvatar";
        String NICKNAME = "nickName";
        String SUPERTYPE = "supertype";
        String LOGIN_TYPE = "loginType";
        String LOGIN_TOKEN = "loginToken";
    }

    interface User {
        String SP_USER_INFO = "SP_USER_INFO";
        String UID = "uid";
        String TOKEN = "token";
        String LOGIN_TYPE = "loginType";
        String LOGIN_TOKEN = "loginToken";

        String AVATAR = "avatar";
        String NICKNAME = "nickname";
        String SEX = "sex";
        String SUMMARY = "summary";
        String BIRTHDAY = "birthday";
        String ADDRESS = "address";

        String WORK_NUM = "worksNum";
        String FORWARD_NUM = "forwardNum";
        String FOLLOW_NUM = "followNum";
        String FANS_NUM = "fansNum";

        String WORK_LIKE_NUM = "work_like_num";
        String USER_TYPE = "user_type";

        String VERIFIEDREASON = "verifiedReason";

        String LEVEL = "level";
        String LEVEL_EXP = "level_exp";

        String IS_PHONE = "is_phone";
    }

    //登录类型
    interface LoginType {
        String QQ = "qq";
        String WEIXIN = "weixin";
        String SINA = "sina";
        String PHONE = "phone";
    }

    interface MakeFace {

        int clearView = -1;//清除container
        int makeText = 0;// 文字
        int makeFont = 1;// 字体
        int showPickView = 2;// 显示 色盘//改变字体颜色
        int dismissPickView = 3;// 关闭 色盘
        int sendTextToEdit = 4;// 发送文字到 文本编辑到的editext
        int sendTextToStickView = 5;// 发送文字到 stickView
        int makeFontColor = 6;// 字体颜色
        int makeFontPickerColor = 7;//色板字体颜色
        int makeTextBoldWith = 8;// 字体粗细
        int makeTextAlpha = 9;//字体透明变化

        int makeTextStraw = 10;//吸色管
        int makeTextBuddle = 11;//气泡
        int makeTextStrokeWidth = 12;//描边粗细
        int makeTextStrokeAlphal = 13;//描边透明度
        int makeTextStrokeColor = 14;//描边颜色
        int showPickView_OutLine = 15;// 显示 色盘// 改变 轮廓颜色
        int showPickView_stroke_font = 16;// 显示 色盘// 改变 轮廓颜色
        // int dismissSourceShopView = 17;//隐藏历史或横向素材 view
        int sourceUrl = 18;//选择素材的url
        int sourceUrlHistory = -18;//选择素材的url
        int copyView = 19;//复制图层
        int down = 20;//向下一层
        int up = 21;//向上一层
        int brush = 23;//点击了画笔
        int brush_finish = -23;//画笔展开的时候点击，完成绘制
        int erase = 24;//擦除
        int painwith = 25;// 画笔粗细
        int addLoaclImageHistory = -27;// 添加本地图片到画布
        int addLoaclImageJson = -26;// 添加本地图片到画布
        int dismissCropView = 27;// 移除 裁剪view
        int dismissFilterView = 28;// 移除 滤镜view
        int filter = 29;// 滤镜 调节
        int chooseFilter = 30;//点击选择滤镜
        int showCropMask = 31;//显示裁剪蒙板
        int paintColor = 33;//涂鸦画笔颜色
        int paintPickerColor = 34;//画笔色板选择的颜色
        int paintStyle = 35;//图片画笔风格
        int paintMasksTyle = 36;//荧光笔风格
        int paintAlpha = 37;//涂鸦画笔的透明度
        int Mosaic = 38;//添加马赛克图层
        int painGetColor = 40;// 画笔粗细
        int imageMaterial = 41;
        int imageMaterialHistory = 42;//选择素材的url
        int imageMaterialLocal = 43;
        int textAnimation = 44;// 字体粗细
//        int textColorSize = 45;// 同步颜色百分比（内描边）
//        int textStrokeSize = 46;// 同步描边百分比（内描边）
    }

    interface Info {
        int SEX = 00;
        int NICKNAME = 01;
        int BIRTHDAY = 02;
        int LOCATION = 03;
        int SUMMARY = 04;
    }

    interface TUYA {
        int UNSelectFistRowColor = -1;
        int UNSelectALl = -2;
        int STROKEWIDth = 4;
        int PAINTWIDTH = 2;
        String ADD_TuyaBitmap = "ADD_TuyaBitmap";
        String ADD_MosaicBitmap = "ADD_MosaicBitmap";
        String TUYALAYER_FILENAME = "TuyaLayer";
    }

    interface STICKER {
        int GIFWIDTH = 100;
        int GIFHEIGHT = 100;
    }

    interface APP {
        String APPNAME = "表情说说";
        int GIFHEIGHT = 100;
    }

    interface HISTORY {
        int STICKER_HISTORY = 1;
        int TUYA_HISTORY = 2;
        int CAMERA_SOURCE_HISTORY = 3;
        int IMAGE_HISTORY = 4;
    }

    interface STICKER_ACTION_RECORD {
        int ACTION_NULL = 0;
        int ACTION_ADD_VIEW = 1;
        int ACTION_MOVE_VIEW = 2;
        int ACTION_ROTATE_VIEW = 3;
        int ACTION_DELETE_VIEW = 4;
        int ACTION_SCALEX_VIEW = 5;
        int ACTION_SCALEY_VIEW = 6;
    }

    /**
     * 发布的时候从相册提取，静态图最小边要小于等于720
     * gif大小必须小于5M
     * 相册提取gif，长和宽都必要小于360
     */
    interface Camerasettings {
        int FALSHMODE_DURATION = 1500;//闪拍
        int FREEDOMMODE_DURATION = 3000;//自由拍摄
        long UNIT_TIME = 3000;//自由拍摄
        //生成视频每秒帧数 30
        //webp质量参数 1
        //动态图每秒帧数 7
        //拍照分辨率 720
        int PHOTO_RESOLUTION = 720;
        int PHOTO_RESOLUTION_WECHAT_SHARE = 300;
        int PHOTO_MAX_RESOLUTION = 720;
        //视频分辨率 480
        int VIDEO_RESOLUTION = 480;
        //分享表情的分辨率  300
        int SHARE_WECHAT_GIF_RESOLUTION = 240;
        int IDEAL_GIF_RESOLUTION = 360;
        int SHARE_RESOLUTION_PICTURE = 360;
        //发布到平台的分辨率 720
        int APPTARGET_RESOLUTION = 720;
        int VIDEO_CROP_THUMB_MIN = 8;
        int GIF_FAMES_NUM_SHARE = 8;
        int GIF_FAMES_NUM_PUBLISH = 8;
        int NormalVideoBitRate = 3000000;
        String FFmpegSplitWord = "_BIAOQING_TECHNOLOGY";
        int keyint = 2;//keyint 每隔多少帧 生产一个关键帧
        int ThumbsResolution = 40;//视频缩率图片的分辨率
        String BIT_RATE_PREVIEW = "3000k";//为了好的预览效果
        String BIT_RATE_WECHAT_SHARE = "2000k";//2000k/1500k 如果原视频码率比较高，体积超过1m
    }

    interface GETFILE_TYPE {
        int SHARE_TEXTLAYER = 1;
        int SHARE_GIF = 2;
        int SHARE_MP4 = 3;
    }

    interface UpUploadFolderPath
    {
        String STUFF = "stuff/";
        String WORK = "work/";//作品 单号固定000 多作品：000～008
        String WORK_DEBUG = "test/work/";//作品 单号固定000 多作品：000～008
        String BG_VIDEO = "video/";//底图是视频 固定000 3个0
        String BG_VIDEO_DEBUG = "test/video/";
        String BG_STATIC_PIC = STUFF;//底图是图片 固定0000  4个0
        String BG_STATIC_PIC_DEBUG = "test/" + STUFF;
        String STICKER_CROP = STUFF;//裁剪的图片 000 叠加 ，从0开始
        String STICKER_TUYA = STUFF;//涂鸦图片   000 叠加  ，从0开始
        String STICKER_DEBUG = "test/" + STUFF;//作品 单号固定000 多作品：000～008

        String VERIFY = "verify/";//认证
        String VERIFY_DEBUG = "test/verify/";//认证

        String AVATAR = "avatar/";//头像
        String AVATAR_DEBUG = "test/avatar/";//头像
    }

    interface FFmpegActionType {
        int CROP_GET_POSITIVE_URL = 1;
        int CROP_VIDEO_DURATION = 2;
        int OPERATE_VIDEO_PRODUCE_GIF = 3;//生产gif
    }

    interface Matisse_RequestCode {
        String Matisse_RequestCode = "Matisse_RequestCode";
        int fromCamera = 11;
        int fromMainPlus = 22;//可能是首页，可能是有头部的专题页面，也可能是没有头部的专题页面
        int fromPublish = 33;
        int fromUserPortrait = 44;
        int fromDiy = 55;
        int fromRegisterPortrait = 66;
        int fromDiyCut = 77;
    }

    interface Camera_Purpose//拍照的目的
    {
        String CAMERA_PURPOSE_TAG = "CAMERA_PURPOSE_TAG";
        int fromMainPlus = 1;//可能是首页，可能是有头部的专题页面，也可能是没有头部的专题页面
        int fromDiy = 2;
    }

    interface PublishFromTag {
        String fromDraft = "fromDraft";
        String fromCamera = "fromCamera";
        String fromMake = "fromMake";
        String fromIndexPlus = "fromIndexPlus";
    }

    interface CAMERA_STATE_SAVE {
        String isOPEN_LANGUAGE_RECOGNIZE = "isOPEN_LANGUAGE_RECOGNIZE";
        String isOPEN_BEAUTY_MODE = "isOPEN_BEAUTY_MODE";
        String isBackForward = "isBackForward";
        String isPhotoOrVideo = "isPhotoOrVideo";
    }

    interface ACTIVITY_REQUEST_CODE {
        int PUBLISH_PHOTOALBUM = 1;
    }

    interface INTENT_TAG {
        String PUBLISH_TOPIC_DATA = "PUBLISH_TOPIC_DATA";
        String PUBLISH_ATE_DATA = "PUBLISH_ATE_DATA";
    }

    interface ADDRES_FROM_PHOTO {
        String FROM_WHERE = "FROM_WHERE";
        int FROM_CAMERA = 1;
        int FROM_PUBLISH = 2;
        int FROM_CANVAS = 3;
        int FROM_PUBLISH_PREVIEW = 4;
    }

    /**
     * 图片展示模式
     */
    interface PhotoShowMode {
        String HIGH_EFFECT = "high_effect";
        String LOW_EFFECT = "low_effect";
    }

    /**
     * 图片展示模式
     */
    interface WaterMarkType {
        String WATER_MARK_NONE = "water_mark_none";
        String WATER_MARK_LOGO = "water_mark_logo";
        String WATER_MARK_LOGO_NAME = "water_mark_logo_name";
        String WATER_MARK_LOGO_USERNAME = "water_mark_logo_username";
        String WATER_MARK_NAME = "water_mark_name";
    }

    /**
     * 区分预览界面保存到本地，分享，和准备发布的动作
     */

    interface CameraPreviewOperateType {
        int Share = 1;
        int SaveLocal = 2;
        int Publish = 3;
        int Diy = 4;//预览页面进入制作
        int Diy_ADD_CANVAS = 5;//制作页面添加相册
    }

    /**
     * 拍摄保存的文件名
     * * 一个视频，normalvideo.mp4
     * 倒叙视频，reverse.mp4
     * 乒乓视频，pingpang.mp4
     * 裁剪的视频，crop.mp4
     * 更改分辨率的视频，resolution.mp4
     * 合成的视频，merge.mp4
     * 截取的水印层，overlay.png
     * 更改分辨率的水印层 overlay4video.png
     * gif图层，final.gif
     * <p>
     * Photo:
     * (不像视频那么复杂，我们区块截屏，然后再调整分辨率就好)
     * 一张图片，normalphoto.png
     * 合成的照片 mergephoto.png
     * 调整分辨率的照片 finalPhoto.png
     */
    interface CameraFileName {
        interface CameraFileNameTag {
            int NORMAL_VIDEO = 1;
            int REVERSE = 2;
            int PINGPANG = 3;
            int CROP_VIDEO_SIZE = 4;
            int RESOLUTION_VIDEO = 5;
            int MERGE_VIDEO = 6;
            int OVERLAY = 7;
            int OVERLAY4VIDEO = 8;
            int FINALGIF = 9;
            int NORMAL_PHOTO = 10;
            int MERGE_PHOTO = 11;
            int FINALPHOTO = 12;
            int CROP_VIDEO_DURATION = 13;
            int SPEED_VIDEO = 14;
            int GIF_COLOUR_BOARD = 15;
            int IMG_WATERMARK_IOS_ORIGINAL = 16;
            int IMG_WATERMARK_IOS_ADJUST = 17;
            int NORMAL_VIDEO_PLUS_KEY_FRAME = 18;
            int POSITIVE_TS = 19;
            int REVERSE_TS = 20;
        }

        String NORMAL_VIDEO = "normalvideo.mp4";
        String NORMAL_VIDEO_PLUS_KEY_FRAME = "normalvideo_keyframe.mp4";
        String REVERSE = "reverse.mp4";
        String PINGPANG = "pingpang.mp4";
        String CROP_VIDEO_SIZE = "cropsize.mp4";
        String RESOLUTION_VIDEO = "resolution.mp4";
        String MERGE_VIDEO = "merge.mp4";
        String OVERLAY = "overlay.png";
        String OVERLAY4VIDEO = "overlay4video.png";
        String FINALGIF = "final.gif";
        String NORMAL_PHOTO = "normalphoto.png";
        String MERGE_PHOTO = "mergephoto.png";
        String FINALPHOTO = "finalphoto.png";
        String CROP_VIDEO_DURATION = "cropduration.mp4";
        String SPEED_VIDEO = "speed_video.mp4";
        String GIF_COLOUR_BOARD = "gif_colour_board.png";
        String IMG_WATERMARK_IOS_ORIGINAL = "img_watermark_ios_original.png";
        String IMG_WATERMARK_IOS_ADJUST = "img_watermark_ios_adjust.png";
        String POSITIVE_TS = "postive.ts";
        String REVERSE_TS = "reverse.ts";
    }

    interface FileSuffix {
        String JPG = "jpg";
        String GIF = "gif";
        String PNG = "png";
        String MP4 = "mp4";
        String WEBP = "webp";
    }

    interface contentViewType {
        //本地素材 绘图 要上传图片
        int ContentViewTypeImage = 0; //网络素材
        int ContentViewTypeLocaImage = 1;//本地素材
        int ContentViewTypeTextbox = 2;//文字
        int ContentViewTypeDraw = 3;//涂鸦，绘图
    }

    interface ToolViewsType {
        //本地素材 绘图 要上传图片
        int ButtonPositionTypeTop = 0;
        int ButtonPositionTypeLeft = 1;
        int PositionTypeBottom = 2;
        int ButtonPositionTypeRight = 3;
    }

    interface HistoryWeiXinLogin {
        String PREFS_HISTORY_WEIXIN_LOGIN = "prefs_history_weixin_login";
        String ACCESS_TOKEN = "access_token";
        String AVATAR = "avatar";
        String NICKNAME = "nickname";
        String TOKEN = "token";
        String LOGIN_TIME = "login_time";
        String LOGIN_TYPE = "login_type";

        String USER_ID = "user_id";
    }

    interface HistoryWeiBoLogin {
        String PREFS_HISTORY_WEIBO_LOGIN = "prefs_history_weibo_login";
        String ACCESS_TOKEN = "access_token";
        String AVATAR = "avatar";
        String NICKNAME = "nickname";
        String TOKEN = "token";
        String LOGIN_TIME = "login_time";
        String LOGIN_TYPE = "login_type";

        String USER_ID = "user_id";
    }

    interface HistoryQQLogin {
        String PREFS_HISTORY_QQ_LOGIN = "prefs_history_qq_login";
        String ACCESS_TOKEN = "access_token";
        String AVATAR = "avatar";
        String NICKNAME = "nickname";
        String TOKEN = "token";
        String LOGIN_TIME = "login_time";
        String LOGIN_TYPE = "login_type";

        String USER_ID = "user_id";
    }

    interface HistoryPhoneLogin {
        String PREFS_HISTORY_PHONE_LOGIN = "prefs_history_phone_login";
        String ACCESS_TOKEN = "access_token";
        String AVATAR = "avatar";
        String NICKNAME = "nickname";
        String TOKEN = "token";
        String LOGIN_TIME = "login_time";
        String LOGIN_TYPE = "login_type";
        String PHONE = "phone";

        String USER_ID = "user_id";
    }

    interface PermissionCode {
        int UseCamera = 100;
        int UseAlbums = 200;
        int RequestPermissionOnStart = 300;
        int UseContact = 400;
    }

    interface UmengManager {
        boolean isrelease = true;//是否调用 UMShareAPI.get(this).release();
        boolean isrelease4SubjectPreview = false;//表情详情和表情预览的界面，两处分享回收友盟资源，异常可能性大
    }
}
