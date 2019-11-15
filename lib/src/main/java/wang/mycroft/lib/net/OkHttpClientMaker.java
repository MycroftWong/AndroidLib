package wang.mycroft.lib.net;

import okhttp3.OkHttpClient;

/**
 * {@link OkHttpClient}构造接口
 *
 * @author Mycroft Wong
 * @date 2019年11月15日
 */
public interface OkHttpClientMaker {

    /**
     * 构造OkHttpClient
     *
     * @return {@link OkHttpClient}
     */
    OkHttpClient makeOkHttpClient();
}
