package my.rpc.interfaces;

import java.util.List;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/9 17:00
 * @Description 业务层
 **/
public interface DataService {

    /**
     * 发送数据
     *
     * @param body 消息体
     * @return 响应体
     */
    String sendData(String body);

    /**
     * 获取数据
     *
     * @return 数据内容
     */
    List<String> getList();
}
