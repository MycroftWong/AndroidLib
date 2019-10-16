package wang.mycroft.lib.sample.repository.model;

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13日
 * @author: wangqiang
 */
public interface ResultModel<T> {

    /**
     * 正确的码
     */
    int CODE_SUCCESS = 0;

    /**
     * 获取错误码，0表示无错误
     *
     * @return error code
     */
    int getErrorCode();

    /**
     * 获取错误信息
     *
     * @return error msg
     */
    String getErrorMsg();

    /**
     * 获取真实数据
     *
     * @return data
     */
    T getData();
}
