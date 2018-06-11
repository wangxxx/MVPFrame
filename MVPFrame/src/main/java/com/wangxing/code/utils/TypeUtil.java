package com.wangxing.code.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by WangXing on 2017/8/31.
 */

public class TypeUtil {

    public static <T> T getTypeObject(Object o, int i) {

        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }

}
