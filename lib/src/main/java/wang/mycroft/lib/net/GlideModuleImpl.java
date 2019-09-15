package wang.mycroft.lib.net;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.mycroft.lib.net.RemoteService;

import java.io.InputStream;

/**
 * 自定义的okhttp glide module, 为了重用okhttp
 *
 * @author mycroft
 */
@GlideModule
public class GlideModuleImpl extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RemoteService.Companion.getImpl().getHttpClient()));

    }
}
