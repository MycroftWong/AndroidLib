package wang.mycroft.lib.sample.component

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

/**
 * 和{@link wang.mycroft.lib.sample.ui.activity.WebViewActivity}在同一进程，用于提前启动进程，加快第一次访问速度
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class WebService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {

        fun start(context: Context) {
            context.startService(Intent(context, WebService::class.java))
        }
    }
}