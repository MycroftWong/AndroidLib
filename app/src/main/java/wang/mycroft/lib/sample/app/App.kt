package wang.mycroft.lib.sample.app

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.didichuxing.doraemonkit.DoraemonKit
import com.tencent.smtt.sdk.QbSdk
import wang.mycroft.lib.sample.ui.activity.ArticleWebViewActivity
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

        DoraemonKit.install(this)

        DoraemonKit.setWebDoorCallback { context, url ->
            context.startActivity(ArticleWebViewActivity.getIntent(context, "哆啦A梦", url))
        }
    }
}