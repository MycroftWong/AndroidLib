package wang.mycroft.lib.net

import okhttp3.OkHttpClient

/**
 * [OkHttpClient]构造器
 *
 * @author Mycroft Wong
 * @date 2019年11月15日
 */
interface OkHttpClientMaker {
    /**
     * 构造[OkHttpClient]
     *
     * @return [OkHttpClient]
     */
    fun makeOkHttpClient(): OkHttpClient
}