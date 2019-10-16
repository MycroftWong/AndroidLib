package wang.mycroft.lib.sample.service

import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.Utils
import wang.mycroft.lib.sample.component.BackgroundService
import wang.mycroft.lib.sample.dao.AppDatabase
import wang.mycroft.lib.sample.dao.HistoryKeyDao
import wang.mycroft.lib.sample.model.HistoryKey

/**
 * 搜索历史dao
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
object HistoryKeyServiceImpl : IHistoryKeyService {

    private val historyKeyDao: HistoryKeyDao = AppDatabase.getHistoryKeyDao()

    override fun addHistoryKey(key: String) {
        BackgroundService.addHistoryKey(Utils.getApp(), key)
    }

    override fun clearHistoryKey() {
        BackgroundService.clearHistoryKey(Utils.getApp())
    }

    override fun deleteHistoryKey(historyKey: HistoryKey) {
        BackgroundService.deleteHistoryKey(Utils.getApp(), historyKey)
    }

    override fun getAllHistoryKey(): LiveData<List<HistoryKey>> {
        return historyKeyDao.getAllHistoryKey()
    }
}