package wang.mycroft.lib.sample.service

import com.blankj.utilcode.util.Utils
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wang.mycroft.lib.sample.component.BackgroundService
import wang.mycroft.lib.sample.dao.AppDatabase
import wang.mycroft.lib.sample.dao.HistoryKeyDao
import wang.mycroft.lib.sample.model.HistoryKey

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
object HistoryKeyServiceImpl : IHistoryKeyService {

    private val historyKeyDao: HistoryKeyDao

    init {
        val appDatabase = AppDatabase.getInstance()
        historyKeyDao = appDatabase.historyKeyDao()
    }

    override fun addHistoryKey(key: String) {
        BackgroundService.addHistoryKey(Utils.getApp(), key)
    }

    override fun clearHistoryKey() {
        BackgroundService.clearHistoryKey(Utils.getApp())
    }

    override fun deleteHistoryKey(historyKey: HistoryKey) {
        BackgroundService.deleteHistoryKey(Utils.getApp(), historyKey)
    }

    override fun getAllHistoryKey(): Flowable<List<HistoryKey>> {
        return historyKeyDao.allHistoryKey
            .compose(async())
    }

    private fun <T> async(): FlowableTransformer<T, T> {
        return FlowableTransformer<T, T> { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}