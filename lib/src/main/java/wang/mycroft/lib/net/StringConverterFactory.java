package wang.mycroft.lib.net;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * String的Converter
 *
 * @author Mycroft_Wong
 * @date 2016/3/23
 */
public final class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    private StringConverterFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return value -> {
                try (ResponseBody body = value) {
                    return body.string();
                }
            };
        }
        return null;
    }
}
