package com.my.rpc.core.common;

import java.util.Arrays;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 10:36
 * @Description 核心请求数据
 **/
public class RpcInvocation {

    // 请求的目标方法
    private String targetMethod;

    // 请求的目标服务名称
    private String targetServiceName;

    // 请求参数信息
    private Object[] args;

    private String uuid;

    // 响应体
    private Object Response;

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetServiceName() {
        return targetServiceName;
    }

    public void setTargetServiceName(String targetServiceName) {
        this.targetServiceName = targetServiceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getResponse() {
        return Response;
    }

    public void setResponse(Object response) {
        Response = response;
    }

    @Override
    public String toString() {
        return "RpcInvocation{" +
                "targetMethod='" + targetMethod + '\'' +
                ", targetServiceName='" + targetServiceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", uuid='" + uuid + '\'' +
                ", Response=" + Response +
                '}';
    }
}
