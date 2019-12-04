package wang.mycroft.lib.sample.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import wang.mycroft.lib.sample.model.HistoryKey

/**
 * Room数据库
 *
 * @author Mycroft Wong
 * @date 2019年10月16日
 */
@Database(entities = [HistoryKey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "wanandroid.db"

        private val appDatabase: AppDatabase by lazy {
            Room.databaseBuilder(Utils.getApp(), AppDatabase::class.java, DB_NAME).build()
        }

        fun getHistoryKeyDao(): HistoryKeyDao {
            return appDatabase.historyKeyDao()
        }
    }

    abstract fun historyKeyDao(): HistoryKeyDao
}