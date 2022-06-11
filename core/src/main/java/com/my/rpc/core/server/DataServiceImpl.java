package com.my.rpc.core.server;

import my.rpc.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 11:23
 * @Description 业务层实现
 **/
public class DataServiceImpl implements DataService {
    @Override
    public String sendData(String body) {
        System.out.println("已收到参数的长度：" + body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a1");
        list.add("b2");
        list.add("c3");
        return list;
    }
}
