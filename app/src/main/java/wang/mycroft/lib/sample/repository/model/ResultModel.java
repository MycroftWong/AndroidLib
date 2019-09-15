package wang.mycroft.lib.sample.repository.model;

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13
 * @author: wangqiang
 */
public interface ResultModel<T> {

    int getErrorCode();

    String getErrorMsg();

    T getData();
}
