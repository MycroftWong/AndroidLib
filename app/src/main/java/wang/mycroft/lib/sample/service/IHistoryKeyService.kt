package wang.mycroft.lib.sample.service

import androidx.lifecycle.LiveData
import wang.mycroft.lib.sample.model.HistoryKey

/**
 * 历史搜索service
 *
 * @author Mycroft Wong
 * @date 2019年10月16日
 */
interface IHistoryKeyService {

    /**
     * 添加历史搜索关键词
     *
     * @param key 搜索关键字
     */
    fun addHistoryKey(key: String)

    /**
     * 清空历史搜索关键字
     */
    fun clearHistoryKey()

    /**
     * 删除历史搜索关键字
     *
     * @param historyKey 历史搜索关键字
     */
    fun deleteHistoryKey(historyKey: HistoryKey)

    /**
     * 获取搜索历史关键词
     *
     * @return 历史关键字列表 Flowable
     */
    fun getAllHistoryKey(): LiveData<List<HistoryKey>>
}