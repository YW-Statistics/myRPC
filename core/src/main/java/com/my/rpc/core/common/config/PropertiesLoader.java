package com.my.rpc.core.common.config;

import io.netty.util.internal.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/18 16:11
 * @Description 配置加载器
 **/
public class PropertiesLoader {

    private static Properties properties;

    private static Map<String, String> propertiesMap = new HashMap<>();

    private static String DEFAULT_PROPERTIES_FILE = "D:\\Privacy\\Wen\\Learning\\Java\\myRPC\\core\\src\\main\\resources\\rpc.properties";

    /**
     * 加载配置
     */
    public static void loadConfiguration() throws IOException {
        if (null != properties) {
            return;
        }
        properties = new Properties();
        FileInputStream in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
        properties.load(in);
        in.close();
    }

    /**
     * 根据键值获取属性
     */
    public static String getPropertiesStr(String key) {
        if (StringUtil.isNullOrEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return propertiesMap.get(key);
    }

    /**
     * 根据键值获取属性
     */
    public static Integer getPropertiesInteger(String key) {
        if (StringUtil.isNullOrEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }
}
