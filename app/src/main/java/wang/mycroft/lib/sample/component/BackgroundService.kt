package wang.mycroft.lib.sample.component

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.AppUtils
import io.reactivex.Flowable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import wang.mycroft.lib.sample.dao.AppDatabase
import wang.mycroft.lib.sample.dao.HistoryKeyDao
import wang.mycroft.lib.sample.model.HistoryKey

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */

class BackgroundService : IntentService(AppUtils.getAppName() + ".background") {

    private var historyKeyDao: HistoryKeyDao? = null

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val appDatabase = AppDatabase.getInstance()
            historyKeyDao = appDatabase.historyKeyDao()

            val action = intent.action
            if (ACTION_ADD == action) {
                val key = intent.getStringExtra(EXTRA_HISTORY_KEY)
                addHistory(key)
            } else if (ACTION_CLEAR == action) {
                clearHistory()
            } else if (ACTION_DELETE == action) {
                val historyKey = intent.getParcelableExtra<HistoryKey>(EXTRA_HISTORY_KEY)
                deleteHistoryKey(historyKey)
            }
        }
    }

    /**
     * 添加搜索关键字
     *
     * @param key 搜索关键字
     */
    private fun addHistory(key: String) {
        val historyKey = HistoryKey()
        historyKey.key = key
        historyKey.date = System.currentTimeMillis()

        val queried = historyKeyDao!!.getHistoryKey(key)
        val flowable: Flowable<Long>
        if (queried == null) {
            flowable = Flowable.just(historyKeyDao!!.add(historyKey)!!)
        } else {
            historyKey.id = queried.id
            flowable = Flowable.just(historyKeyDao!!.updateHistoryKey(historyKey).toLong())
        }
        flowable.subscribe(INSTANCE)
    }

    /**
     * 清空搜索历史关键字
     */
    private fun clearHistory() {
        historyKeyDao!!.allHistoryKey
            .map { historyKeys -> historyKeyDao!!.deleteAll(historyKeys).toLong() }
            .subscribe(INSTANCE)
    }

    /**
     * 删除历史关键字
     *
     * @param historyKey 历史关键字
     */
    private fun deleteHistoryKey(historyKey: HistoryKey) {
        Flowable.just(historyKeyDao!!.delete(historyKey))
            .map { it.toLong() }
            .subscribe(INSTANCE)
    }

    companion object {

        private val ACTION_ADD = AppUtils.getAppPackageName() + ".add.action"

        private val ACTION_CLEAR = AppUtils.getAppPackageName() + ".clear.action"
        private val ACTION_DELETE = AppUtils.getAppPackageName() + ".delete.action"

        private const val EXTRA_HISTORY_KEY = "history_key.extra"

        fun addHistoryKey(context: Context, key: String) {
            val intent = Intent()
            intent.setPackage(context.packageName)
            intent.action = ACTION_ADD
            intent.putExtra(EXTRA_HISTORY_KEY, key)
            context.startService(intent)
        }

        fun clearHistoryKey(context: Context) {
            val intent = Intent()
            intent.setPackage(context.packageName)
            intent.action = ACTION_CLEAR
            context.startService(intent)
        }

        fun deleteHistoryKey(context: Context, historyKey: HistoryKey) {
            val intent = Intent()
            intent.setPackage(context.packageName)
            intent.action = ACTION_DELETE
            intent.putExtra(EXTRA_HISTORY_KEY, historyKey)
            context.startService(intent)
        }

        /**
         * 处理异常等信息的空Subscriber
         */
        private val INSTANCE = object : Subscriber<Long> {

            override fun onSubscribe(s: Subscription) {

            }

            override fun onNext(aLong: Long?) {

            }

            override fun onError(t: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }
}