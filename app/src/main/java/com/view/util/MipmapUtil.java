package com.view.util;


import com.season.emoji.R;

import java.lang.reflect.Field;

public class MipmapUtil {
    public static int  getResource(String imageName){
        Class mipmap = R.mipmap.class;
        try {
            Field field = mipmap.getField(imageName);
            int resId = field.getInt(imageName);
            return resId;
        } catch (NoSuchFieldException e) {//如果没有在"mipmap"下找到imageName,将会返回0
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }

    }
}
