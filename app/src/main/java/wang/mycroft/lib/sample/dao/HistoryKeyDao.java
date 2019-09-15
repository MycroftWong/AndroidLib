package wang.mycroft.lib.sample.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import wang.mycroft.lib.sample.model.HistoryKey;

/**
 * 搜索历史dao
 *
 * @author wangqiang
 */
@Dao
public interface HistoryKeyDao {

    /**
     * 查询所有的搜索历史记录
     *
     * @return flowable
     */
    @Query("SELECT * FROM history_key ORDER BY date DESC LIMIT 10")
    Flowable<List<HistoryKey>> getAllHistoryKey();

    /**
     * 添加历史记录
     *
     * @param historyKey 历史搜索
     * @return row_id
     */
    @Insert
    Long add(HistoryKey historyKey);

    /**
     * 删除所有历史记录
     *
     * @param historyKeys 历史搜索记录
     * @return row affected
     */
    @Delete
    int deleteAll(List<HistoryKey> historyKeys);

    /**
     * 查找搜索历史记录
     *
     * @param key 搜索关键词
     * @return 搜索历史记录
     */
    @Query("SELECT * FROM history_key WHERE `key`=:key")
    HistoryKey getHistoryKey(String key);

    /**
     * 更新历史记录
     *
     * @param historyKey 历史记录
     * @return row_id
     */
    @Update
    int updateHistoryKey(HistoryKey historyKey);

    /**
     * 删除历史记录
     *
     * @param historyKey 历史记录
     * @return row_id
     */
    @Delete
    int delete(HistoryKey historyKey);
}
