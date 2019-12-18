package wang.mycroft.lib.sample.component

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import wang.mycroft.lib.sample.dao.AppDatabase
import wang.mycroft.lib.sample.dao.HistoryKeyDao
import wang.mycroft.lib.sample.model.HistoryKey

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15日
 * @author: wangqiang
 */

class BackgroundService : IntentService(AppUtils.getAppName() + ".background") {

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
    }

    private var historyKeyDao: HistoryKeyDao? = null

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            historyKeyDao = AppDatabase.getHistoryKeyDao()

            GlobalScope.launch(Dispatchers.IO) {
                when (intent.action) {
                    ACTION_ADD -> {
                        val key = intent.getStringExtra(EXTRA_HISTORY_KEY)
                        addHistory(key)
                    }
                    ACTION_CLEAR -> clearHistory()
                    ACTION_DELETE -> {
                        val historyKey = intent.getParcelableExtra<HistoryKey>(EXTRA_HISTORY_KEY)
                        deleteHistoryKey(historyKey)
                    }
                }
            }
        }
    }

    /**
     * 添加搜索关键字
     *
     * @param key 搜索关键字
     */
    private suspend fun addHistory(key: String) {
        val historyKey = HistoryKey()
        historyKey.key = key
        historyKey.date = System.currentTimeMillis()

        val queried = historyKeyDao!!.getHistoryKey(key)
        if (queried == null) {
            historyKeyDao!!.add(historyKey)
        } else {
            historyKey.id = queried.id
            historyKeyDao!!.updateHistoryKey(historyKey)
        }
    }

    /**
     * 清空搜索历史关键字
     */
    private suspend fun clearHistory() {
        historyKeyDao!!.deleteAll(historyKeyDao!!.loadAllHistoryKey())
    }

    /**
     * 删除历史关键字
     *
     * @param historyKey 历史关键字
     */
    private suspend fun deleteHistoryKey(historyKey: HistoryKey) {
        historyKeyDao!!.delete(historyKey)
    }

}