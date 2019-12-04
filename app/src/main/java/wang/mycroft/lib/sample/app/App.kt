package wang.mycroft.lib.sample.app

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.tencent.smtt.sdk.QbSdk
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

        Utils.init(this)

        Loading.initDefault(SpinLoadingAdapter())

        LogUtils.getConfig().isLogSwitch = true

        QbSdk.initX5Environment(this, null)
    }
}