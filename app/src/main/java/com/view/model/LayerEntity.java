package com.view.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongxin on 2017/8/24.
 */
///* 背景webP */
//    //@property(nonatomic ,copy) NSString *webPURLPath;
//
//    /* 比例 */
//    @property(nonatomic ,assign) BQSCustomizingVCProportionType proportionType;
//
///* 存放元素的数组 */
//    @property(nonatomic ,strong) NSMutableArray<CKYRotationViewItem *> *itemArray;
//
//    /* 创作的尺寸 */
//    @property(nonatomic ,assign) CGFloat width;
//        /* 背景颜色 */
//        @property(nonatomic ,strong) NSString *backColorString;
///* 视频资源 */
//        @property(nonatomic ,strong) NSString *assetPath;
//        /* 视频速度 */
//        @property(nonatomic ,assign) CGFloat rate;
///* 图片地址 */
//        @property(nonatomic ,strong) NSString *imgURLPath;
public class LayerEntity implements Serializable {

    @Override
    public String toString()
    {
        return "LayerEntity{" + "backInfoModel=" + backInfoModel + ", width=" + width + ", proportionType=" +
                proportionType + ", itemArray=" + itemArray + '}';
    }

    /**
     * itemArray : [{"xScale":1.467921,"index":0,"vMoveBtnPositionType":2,"hMoveBtnPositionType":3,"centerY":385.25,
     * "contentViewType":2,"turnOverH":false,"angle":0.003607564,"textFontPath":"https: //img.biaoqing
     * .com/font/HappyZcool.ttf","textStyleModel":{"textX":12.38154,"thumbnail":"yangshi0",
     * "styleModel":{"textColor":"ffe63f","strokeColor":"5842ff","textColorSize":0,"miaobianAlpha":1,"textAlpha":1,
     * "strokeSize":0.75},"textH":47.73438,"textW":160.96,"textY":3.671875,"thumbnailW":98,"fontName":"CN000015",
     * "imageName":"","imageWidth":185.7231,"imageHeight":55.07812},"turnOverV":false,"centerX":187.5,"text":"抖起来！",
     * "imageURL":"","textFontName":"CN000015","sizeWidth":185.7231,"sizeHeight":55.07812,"textFontSize":40,
     * "yScale":1.467921}]
     * backInfoModel : {"assetPath":"https: //img.biaoqing.com/video/20170817/16372400064.mp4","backColorString":"",
     * "imgURLPath":"","rate":1}
     * width : 375
     * proportionType : 1
     */
    //    proportionType 比例
    private BackInfoModelBean backInfoModel;
    private float width;
    private float height;
    private int proportionType;
    private List<ItemArrayBean> itemArray;


    public long originId;
    public long originalId;

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public BackInfoModelBean getBackInfoModel()
    {
        return backInfoModel;
    }

    public void setBackInfoModel(BackInfoModelBean backInfoModel)
    {
        this.backInfoModel = backInfoModel;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public int getProportionType()
    {
        return proportionType;
    }

    public void setProportionType(int proportionType)
    {
        this.proportionType = proportionType;
    }

    public List<ItemArrayBean> getItemArray()
    {
        return itemArray;
    }

    public void setItemArray(List<ItemArrayBean> itemArray)
    {
        this.itemArray = itemArray;
    }

    public static class BackInfoModelBean implements Serializable {
        /**
         * assetPath : https: //img.biaoqing.com/video/20170817/16372400064.mp4
         * backColorString :
         * imgURLPath :
         * rate : 1
         */

        String assetPath = "";
        String backColorString = "";
        String imgURLPath = "";
        float rate;
        public String assetPathFile;
        public String imageURLPathFile;

        public String orignalVideoUrl;
        public String orignalImageUrl;

        public String getAssetPath()
        {
            return assetPath;
        }

        public void setAssetPath(String assetPath)
        {
            this.assetPath = assetPath;
        }

        public String getBackColorString()
        {
            return backColorString;
        }

        public void setBackColorString(String backColorString)
        {
            this.backColorString = backColorString;
        }

        public String getImgURLPath()
        {
            return imgURLPath;
        }

        public void setImgURLPath(String imgURLPath)
        {
            this.imgURLPath = imgURLPath;
        }

        public float getRate()
        {
            return rate;
        }

        public void setRate(float rate)
        {
            this.rate = rate;
        }
    }

    public static class ItemArrayBean implements Serializable {

        /**
         * xScale : 1.467921
         * index : 0
         * vMoveBtnPositionType : 2
         * hMoveBtnPositionType : 3
         * centerY : 385.25
         * contentViewType : 2
         * turnOverH : false
         * angle : 0.003607564
         * textFontPath : https: //img.biaoqing.com/font/HappyZcool.ttf
         * textStyleModel : {"textX":12.38154,"thumbnail":"yangshi0","styleModel":{"textColor":"ffe63f",
         * "strokeColor":"5842ff","textColorSize":0,"miaobianAlpha":1,"textAlpha":1,"strokeSize":0.75},
         * "textH":47.73438,"textW":160.96,"textY":3.671875,"thumbnailW":98,"fontName":"CN000015","imageName":"",
         * "imageWidth":185.7231,"imageHeight":55.07812}
         * turnOverV : false
         * centerX : 187.5
         * text : 抖起来！
         * imageURL :
         * textFontName : CN000015
         * sizeWidth : 185.7231
         * sizeHeight : 55.07812
         * textFontSize : 40
         * yScale : 1.467921
         */
        //        contentViewType:
        //        CKYCustomizing  ContentViewTypeImage  = 0,
        //        CKYCustomizing  ContentViewTypeLocaImage=1,
        //        CKYCustomizing  ContentViewTypeTextbox=2,
        //        CKYCustomizing  ContentViewTypeDraw=3,
        //        本地素材 绘图 要上传图片

        //        typedef NS_ENUM(NSInteger, CKYRotationViewButtonPositionType) {
        //            CKYRotationViewButtonPositionTypeTop  = 0,
        //                    CKYRotationViewButtonPositionTypeLeft= 1,
        //                    CKYRotationViewButtonPositionTypeBottom= 2,
        //                    CKYRotationViewButtonPositionTypeRight= 3，
        //        };
        private double xScale;
        private int index;
        private int vMoveBtnPositionType;
        private int hMoveBtnPositionType;
        private double centerY;
        private int contentViewType;
        private boolean turnOverH;
        private double angle;
        private String textFontPath = "";
        private TextStyleEntity textStyleModel;
        private boolean turnOverV;
        private double centerX;
        private String text = "";
        private String imageURL = "";
        public String filePath = "";
        private String textFontName = "";
        private double sizeWidth;
        private double sizeHeight;
        private float textFontSize;
        private double yScale;

        public double getXScale()
        {
            return xScale;
        }

        public void setXScale(double xScale)
        {
            this.xScale = xScale;
        }

        public int getIndex()
        {
            return index;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        public int getVMoveBtnPositionType()
        {
            return vMoveBtnPositionType;
        }

        public void setVMoveBtnPositionType(int vMoveBtnPositionType)
        {
            this.vMoveBtnPositionType = vMoveBtnPositionType;
        }

        public int getHMoveBtnPositionType()
        {
            return hMoveBtnPositionType;
        }

        public void setHMoveBtnPositionType(int hMoveBtnPositionType)
        {
            this.hMoveBtnPositionType = hMoveBtnPositionType;
        }

        public double getCenterY()
        {
            return centerY;
        }

        public void setCenterY(double centerY)
        {
            this.centerY = centerY;
        }

        public int getContentViewType()
        {
            return contentViewType;
        }

        public void setContentViewType(int contentViewType)
        {
            this.contentViewType = contentViewType;
        }

        public boolean isTurnOverH()
        {
            return turnOverH;
        }

        public void setTurnOverH(boolean turnOverH)
        {
            this.turnOverH = turnOverH;
        }

        public double getAngle()
        {
            return angle;
        }

        public void setAngle(double angle)
        {
            this.angle = angle;
        }

        public String getTextFontPath()
        {
            return textFontPath;
        }

        public void setTextFontPath(String textFontPath)
        {
            this.textFontPath = textFontPath;
        }

        public TextStyleEntity getTextStyleModel()
        {
            return textStyleModel;
        }

        public void setTextStyleModel(TextStyleEntity textStyleModel)
        {
            this.textStyleModel = textStyleModel;
        }

        public boolean isTurnOverV()
        {
            return turnOverV;
        }

        public void setTurnOverV(boolean turnOverV)
        {
            this.turnOverV = turnOverV;
        }

        public double getCenterX()
        {
            return centerX;
        }

        public void setCenterX(double centerX)
        {
            this.centerX = centerX;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public String getImageURL()
        {
            return imageURL;
        }

        public void setImageURL(String imageURL)
        {
            this.imageURL = imageURL;
        }

        public String getTextFontName()
        {
            return textFontName;
        }

        public void setTextFontName(String textFontName)
        {
            this.textFontName = textFontName;
        }

        public double getSizeWidth()
        {
            return sizeWidth;
        }

        public void setSizeWidth(double sizeWidth)
        {
            this.sizeWidth = sizeWidth;
        }

        public double getSizeHeight()
        {
            return sizeHeight;
        }

        public void setSizeHeight(double sizeHeight)
        {
            this.sizeHeight = sizeHeight;
        }

        public float getTextFontSize()
        {
            return textFontSize;
        }

        public void setTextFontSize(float textFontSize)
        {
            this.textFontSize = textFontSize;
        }

        public double getYScale()
        {
            return yScale;
        }

        public void setYScale(double yScale)
        {
            this.yScale = yScale;
        }

        public static class TextStyleModelBean {
            /**
             * textX : 12.38154
             * thumbnail : yangshi0
             * styleModel : {"textColor":"ffe63f","strokeColor":"5842ff","textColorSize":0,"miaobianAlpha":1,
             * "textAlpha":1,"strokeSize":0.75}
             * textH : 47.73438
             * textW : 160.96
             * textY : 3.671875
             * thumbnailW : 98
             * fontName : CN000015
             * imageName :
             * imageWidth : 185.7231
             * imageHeight : 55.07812
             */

            private double textX;
            private String thumbnail;
            //            private StyleModelBean styleModel;
            private double textH;
            private double textW;
            private double textY;
            private int thumbnailW;
            private String fontName;
            private String imageName;
            private double imageWidth;
            private double imageHeight;

            public double getTextX()
            {
                return textX;
            }

            public void setTextX(double textX)
            {
                this.textX = textX;
            }

            public String getThumbnail()
            {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail)
            {
                this.thumbnail = thumbnail;
            }

            //            public StyleModelBean getStyleModel()
            //            {
            //                return styleModel;
            //            }
            //
            //            public void setStyleModel(StyleModelBean styleModel)
            //            {
            //                this.styleModel = styleModel;
            //            }

            public double getTextH()
            {
                return textH;
            }

            public void setTextH(double textH)
            {
                this.textH = textH;
            }

            public double getTextW()
            {
                return textW;
            }

            public void setTextW(double textW)
            {
                this.textW = textW;
            }

            public double getTextY()
            {
                return textY;
            }

            public void setTextY(double textY)
            {
                this.textY = textY;
            }

            public int getThumbnailW()
            {
                return thumbnailW;
            }

            public void setThumbnailW(int thumbnailW)
            {
                this.thumbnailW = thumbnailW;
            }

            public String getFontName()
            {
                return fontName;
            }

            public void setFontName(String fontName)
            {
                this.fontName = fontName;
            }

            public String getImageName()
            {
                return imageName;
            }

            public void setImageName(String imageName)
            {
                this.imageName = imageName;
            }

            public double getImageWidth()
            {
                return imageWidth;
            }

            public void setImageWidth(double imageWidth)
            {
                this.imageWidth = imageWidth;
            }

            public double getImageHeight()
            {
                return imageHeight;
            }

            public void setImageHeight(double imageHeight)
            {
                this.imageHeight = imageHeight;
            }

            public static class StyleModelBean {
                /**
                 * textColor : ffe63f
                 * strokeColor : 5842ff
                 * textColorSize : 0
                 * miaobianAlpha : 1
                 * textAlpha : 1
                 * strokeSize : 0.75
                 */

                private String textColor;
                private String strokeColor;
                private int textColorSize;
                private int miaobianAlpha;
                private int textAlpha;
                private double strokeSize;

                public String getTextColor()
                {
                    return textColor;
                }

                public void setTextColor(String textColor)
                {
                    this.textColor = textColor;
                }

                public String getStrokeColor()
                {
                    return strokeColor;
                }

                public void setStrokeColor(String strokeColor)
                {
                    this.strokeColor = strokeColor;
                }

                public int getTextColorSize()
                {
                    return textColorSize;
                }

                public void setTextColorSize(int textColorSize)
                {
                    this.textColorSize = textColorSize;
                }

                public int getMiaobianAlpha()
                {
                    return miaobianAlpha;
                }

                public void setMiaobianAlpha(int miaobianAlpha)
                {
                    this.miaobianAlpha = miaobianAlpha;
                }

                public int getTextAlpha()
                {
                    return textAlpha;
                }

                public void setTextAlpha(int textAlpha)
                {
                    this.textAlpha = textAlpha;
                }

                public double getStrokeSize()
                {
                    return strokeSize;
                }

                public void setStrokeSize(double strokeSize)
                {
                    this.strokeSize = strokeSize;
                }
            }
        }
    }
}
