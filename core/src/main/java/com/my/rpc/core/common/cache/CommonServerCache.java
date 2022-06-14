package com.my.rpc.core.common.cache;

import com.my.rpc.core.registry.URL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 10:50
 * @Description 服务端缓存
 **/
public class CommonServerCache {

    public static final Map<String, Object> PROVIDER_CLASS_MAP = new HashMap<>();

    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();

}
