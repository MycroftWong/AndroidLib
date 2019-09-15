package wang.mycroft.lib.sample.app

import android.app.Application
import wang.mycroft.lib.sample.ui.view.SpinLoadingAdapter
import wang.mycroft.lib.view.Loading

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Loading.initDefault(SpinLoadingAdapter())
    }
}