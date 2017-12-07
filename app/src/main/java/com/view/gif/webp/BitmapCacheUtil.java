package com.view.gif.webp;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/1.
 */

public class BitmapCacheUtil {
    private static BitmapCacheUtil defaultInstance;
    public static BitmapCacheUtil getDefault() {
        if (defaultInstance == null) {
            synchronized (BitmapCacheUtil.class) {
                if (defaultInstance == null) {
                    defaultInstance = new BitmapCacheUtil();
                }
            }
        }
        return defaultInstance;
    }

    BitmapCacheUtil(){
        hashMap = new HashMap<>();
    }

    private HashMap<String, BitmapCache> hashMap;
    public BitmapCache decodeFile(String filePath){
        if (hashMap != null && hashMap.get(filePath) != null){
            return hashMap.get(filePath);
        }
        try {
            BitmapCache bitmapCache = new BitmapCache();
            bitmapCache.decodeFile(filePath);
            hashMap.put(filePath, bitmapCache);
            return bitmapCache;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void release(String filePath) {
        hashMap.remove(filePath);
    }
}
