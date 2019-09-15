package wang.mycroft.lib.sample.repository.model;

import wang.mycroft.lib.sample.net.NetModel;

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13
 * @author: wangqiang
 */
public class SimpleResultModel<T> implements ResultModel<T> {

    private final NetModel<T> netModel;

    public SimpleResultModel(NetModel<T> netModel) {
        this.netModel = netModel;
    }

    @Override
    public int getErrorCode() {
        return netModel.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return netModel.getErrorMsg();
    }

    @Override
    public T getData() {
        return netModel.getData();
    }
}
