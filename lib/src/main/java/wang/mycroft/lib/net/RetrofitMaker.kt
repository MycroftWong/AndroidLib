package wang.mycroft.lib.net

import retrofit2.Retrofit

/**
 * 自定义{@link Retrofit}
 *
 * @author Mycroft Wong
 * @date 2016年6月15日
 */
interface RetrofitMaker {
    /**
     * 使用base url构造retrofit
     *
     * @param baseUrl base url
     * @return 构造的retrofit
     */
    fun makeRetrofit(baseUrl: String): Retrofit
}
