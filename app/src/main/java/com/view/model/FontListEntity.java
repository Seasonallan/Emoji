package com.view.model;

import java.util.List;

/**
 * 字体库
 * author：Create linmd on 17/3/2 17:54
 */
public class FontListEntity {


    /**
     * total : 4
     * current : 1
     * pages : 1
     * records : [{"cover":"https://img.biaoqing.com/thumb/font/GJJZHYJW.png","uid":"3","name":"迷你简稚艺","lang":"cn","sample":"https://img.biaoqing.com/thumb/font/sample/GJJZHYJW.png","font":"https://img.biaoqing.com/font/GJJZHYJW.ttf"},{"cover":"https://img.biaoqing.com/thumb/font/STXingkai.png","uid":"1","name":"华文行楷","lang":"cn","sample":"https://img.biaoqing.com/thumb/font/sample/STXingkai.png","font":"https://img.biaoqing.com/font/stxingka.ttf"},{"cover":"https://img.biaoqing.com/thumb/font/GJJCQJW.png","uid":"2","name":"迷你简粗倩","lang":"cn","sample":"https://img.biaoqing.com/thumb/font/sample/GJJCQJW.png","font":"https://img.biaoqing.com/font/GJJCQJW.ttf"},{"cover":"https://img.biaoqing.com/thumb/font/JLinXin.png","uid":"4","name":"迷你简菱心","lang":"cn","sample":"https://img.biaoqing.com/thumb/font/sample/JLinXin.png","font":"https://img.biaoqing.com/font/JLinXin.ttf"}]
     */

    private int total;
    private int current;
    private int pages;
    /**
     * cover : https://img.biaoqing.com/thumb/font/GJJZHYJW.png
     * uid : 3
     * name : 迷你简稚艺
     * lang : cn
     * sample : https://img.biaoqing.com/thumb/font/sample/GJJZHYJW.png
     * font : https://img.biaoqing.com/font/GJJZHYJW.ttf
     */

    private List<RecordsBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        private String fullCover;
        private String uid;
        private String code;
        private String lang;
        private String sample;
        private String fullFont;

        private boolean isDownloading;

        public boolean isDownloading()
        {
            return isDownloading;
        }

        public void setDownloading(boolean download)
        {
            isDownloading = download;
        }

        public String getCover() {
            return fullCover;
        }

        public void setCover(String cover) {
            this.fullCover = cover;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return code;
        }

        public void setName(String name) {
            this.code = name;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getSample() {
            return sample;
        }

        public void setSample(String sample) {
            this.sample = sample;
        }

        public String getFont() {
            return fullFont;
        }

        public void setFont(String font) {
            this.fullFont = font;
        }
    }
}
