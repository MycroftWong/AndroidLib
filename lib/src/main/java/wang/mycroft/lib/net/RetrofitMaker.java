package wang.mycroft.lib.net;

import retrofit2.Retrofit;

/**
 * 自定义{@link Retrofit} 接口
 *
 * @author Mycroft Wong
 * @date 2019年11月15日
 */
public interface RetrofitMaker {

    /**
     * 使用base url构造retrofit
     *
     * @param baseUrl base url
     * @return {@link Retrofit}
     */
    Retrofit makeRetrofit(String baseUrl);
}
