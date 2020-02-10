package wang.mycroft.lib.net

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * String的Converter
 *
 * @author Mycroft Wong
 * @date 2016年6月15日
 */
class StringConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): StringConverterFactory = StringConverterFactory()
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation?>?, retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        return if (type === String::class.java) {
            Converter<ResponseBody, Any> { value: ResponseBody ->
                value.use { it.string() }
            }
        } else null
    }

}