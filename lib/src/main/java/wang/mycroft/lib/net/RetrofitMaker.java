package wang.mycroft.lib.net;

import retrofit2.Retrofit;

/**
 * 自定义{@link Retrofit}
 *
 * @author mycroft
 */
public interface RetrofitMaker {

    /**
     * 使用base url构造retrofit
     *
     * @param baseUrl base url
     * @return 构造的retrofit
     */
    Retrofit makeRetrofit(String baseUrl);
}
