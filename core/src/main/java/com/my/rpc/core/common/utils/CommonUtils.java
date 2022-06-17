package com.my.rpc.core.common.utils;

import java.util.List;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 23:34
 * @Description 公共工具类
 **/
public class CommonUtils {

    public static boolean isEmptyList(List list) {
        if (null == list || list.size() == 0) {
            return false;
        }
        return true;
    }

}
