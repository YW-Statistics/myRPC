package com.my.rpc.core.router;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.registry.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.my.rpc.core.common.cache.CommonClientCache.*;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/21 22:35
 * @Description 随机化路由实现类
 **/
public class RandomRouterImpl implements Router {
    @Override
    public void refreshRouterArr(Selector selector) {
        // 获取服务提供者的数目
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] channelFutureWrapperArr = new ChannelFutureWrapper[channelFutureWrapperList.size()];
        // 提前生成调用先后顺序的数组
        int[] res = createRandomIndex(channelFutureWrapperArr.length);
        // 按照随机数组生成调用顺序
        for (int i = 0; i < res.length; i++) {
            channelFutureWrapperArr[i] = channelFutureWrapperList.get(res[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), channelFutureWrapperArr);
    }

    /**
     * 创建随机数组
     */
    private int[] createRandomIndex(int len) {
        int[] ant = new int[len];
        Arrays.fill(ant, -1);
        Random random = new Random();
        int index = 0;
        while (index < len) {
            int num = random.nextInt(len);
            if (!contains(ant, index, num)) {
                ant[index++] = num;
            }
        }
        return ant;
    }

    private boolean contains(int[] ant, int index, int num) {
        for (int j = 0; j < index; j++) {
            if (ant[j] == num) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {
        // 服务节点的权重
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(url.getServiceName());
        Integer[] weightArr = createWeightArr(channelFutureWrapperList);
        Integer[] finalArr = createRandomArr(weightArr);
        ChannelFutureWrapper[] finalChannelFutureWrapperList = new ChannelFutureWrapper[finalArr.length];
        for (int i = 0;i < finalChannelFutureWrapperList.length;i++) {
            finalChannelFutureWrapperList[i] = channelFutureWrapperList.get(finalArr[i]);
        }

    }

    private Integer[] createRandomArr(Integer[] weightArr) {
        Random random = new Random();
        for (int i = 0; i < weightArr.length; i++) {
            int j = random.nextInt(weightArr.length);
            if (j == i) {
                continue;
            }
            int tmp = weightArr[i];
            weightArr[i] = weightArr[j];
            weightArr[j] = tmp;
        }
        return weightArr;
    }

    /**
     * 创建权重数组
     */
    private Integer[] createWeightArr(List<ChannelFutureWrapper> channelFutureWrapperList) {
        List<Integer> weightList = new ArrayList<>();
        for (int i = 0; i < channelFutureWrapperList.size(); i++) {
            int c = channelFutureWrapperList.get(i).getWeight() / 100;
            for (int j = 0;j < c;j++) {
                weightList.add(i);
            }
        }
        Integer[] weightArr = new Integer[weightList.size()];
        return weightList.toArray(weightArr);
    }
}
