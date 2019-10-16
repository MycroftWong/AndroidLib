package wang.mycroft.lib.sample.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import wang.mycroft.lib.sample.model.HistoryKey

/**
 * 搜索历史DAO
 *
 * @author Mycroft Wong
 * @date 2019年10月16日
 */
@Dao
interface HistoryKeyDao {

    /**
     * 查询所有的搜索历史记录
     *
     * @return flowable
     */
    @Query("SELECT * FROM history_key ORDER BY date DESC LIMIT 10")
    fun getAllHistoryKey(): LiveData<List<HistoryKey>>

    /**
     * 添加历史记录
     *
     * @param historyKey 历史搜索
     * @return row_id
     */
    @Insert
    suspend fun add(historyKey: HistoryKey): Long?

    /**
     * 删除所有历史记录
     *
     * @param historyKeys 历史搜索记录
     * @return row affected
     */
    @Delete
    suspend fun deleteAll(historyKeys: List<HistoryKey>): Int

    /**
     * 查找搜索历史记录
     *
     * @param key 搜索关键词
     * @return 搜索历史记录
     */
    @Query("SELECT * FROM history_key WHERE `key`=:key")
    suspend fun getHistoryKey(key: String): HistoryKey?

    /**
     * 更新历史记录
     *
     * @param historyKey 历史记录
     * @return row_id
     */
    @Update
    suspend fun updateHistoryKey(historyKey: HistoryKey): Int

    /**
     * 删除历史记录
     *
     * @param historyKey 历史记录
     * @return row_id
     */
    @Delete
    suspend fun delete(historyKey: HistoryKey): Int
}