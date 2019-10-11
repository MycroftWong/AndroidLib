package wang.mycroft.lib.sample.net;

/**
 * 统一的数据结构
 *
 * @param <T> 真实数据
 * @author wangqiang
 */
public final class NetModel<T> {

    /**
     * 根据throwable获取NetModel
     *
     * @param throwable throwable
     * @param <R>       得到的数据类型
     * @return NetModel
     */
    public static <R> NetModel<R> error(Throwable throwable) {
        return new NetModel<>(-1, throwable.getMessage(), null);
    }

    /**
     * data : {}
     * errorCode : 0
     * errorMsg :
     */
    private int errorCode;
    private String errorMsg;
    private T data;

    public NetModel() {
    }

    public NetModel(int errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NetModel{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
