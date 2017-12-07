package com.view.model;

import java.io.Serializable;

/**
 * Created by lizhongxin on 2017/5/9.
 * 参考value的textbook.plist（ios对应的mode）进行mode的设置。
 */

public class TextStyleEntity implements Serializable {
    //    <key>imageName</key>//背景图位置
    //		<string></string>
    //		<key>imageWidth</key>//背景图宽度
    //		<integer>0</integer>
    //		<key>imageHeight</key>//高度
    private String imageName="";//背景图资源 气泡框
    private float imageWidth;//背景图的宽
    private float imageHeight;//背景图的高
    private float textX;
    private float textY;
    private float textW;
    public int animationType = 0;
    private float textH;
    private String thumbnail="";//样式展示图，缩率图
    private String fontName="";
    private styleModel styleModel;
    //TODO
    private boolean isDownloading;

    public boolean isDownloading()
    {
        return isDownloading;
    }

    public void setDownloading(boolean download)
    {
        isDownloading = download;
    }

    /**
     *现有的代码逻辑，加入这个地址更好做。但是ios端对应的模型没有这个textFontPath参数。
     */
    private String textFontPath="";
    public String getTextFontPath()
    {
        return textFontPath;
    }
    public void setTextFontPath(String textFontPath)
    {
        this.textFontPath = textFontPath;
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

    public styleModel getStyleModel()
    {
        return styleModel;
    }

    public void setStyleModel(styleModel styleModel)
    {
        this.styleModel = styleModel;
    }

    public float getImageWidth()
    {
        return imageWidth;
    }

    public void setImageWidth(float imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public float getImageHeight()
    {
        return imageHeight;
    }

    public void setImageHeight(float imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public float getTextX()
    {
        return textX;
    }

    public void setTextX(Integer textX)
    {
        this.textX = textX;
    }

    public float getTextW()
    {
        return textW;
    }

    public void setTextW(float textW)
    {
        this.textW = textW;
    }

    public float getTextY()
    {
        return textY;
    }

    public void setTextY(float textY)
    {
        this.textY = textY;
    }

    public float getTextH()
    {
        return textH;
    }

    public void setTextH(Integer textH)
    {
        this.textH = textH;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

//    @Override
//    public String toString()
//    {
//        return "{" + "imageName='" + imageName + '\'' + ", imageWidth=" + imageWidth + ", " +
//                "imageHeight=" + imageHeight + ", textX=" + textX + ", textY=" + textY + ", textW=" + textW + ", " +
//                "textH=" + textH + ", thumbnail='" + thumbnail + '\'' + ", fontName='" + fontName + '\'' + ", " +
//                "styleModel=" + styleModel + ", isDownloading=" + isDownloading + ", textFontPath='" + textFontPath + '\'' + '}';
//    }
}
