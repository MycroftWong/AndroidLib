package wang.mycroft.lib.sample.dao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.blankj.utilcode.util.Utils;

import wang.mycroft.lib.sample.model.HistoryKey;

@Database(entities = {HistoryKey.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "wanandroid.db";

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance() {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(Utils.getApp(),
                    AppDatabase.class, DB_NAME).build();
        }
        return appDatabase;
    }

    public abstract HistoryKeyDao historyKeyDao();

}
