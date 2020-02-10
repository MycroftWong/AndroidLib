package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * 历史搜索关键词
 *
 * @author wangqiang
 */
@Entity(tableName = "history_key", indices = {@Index(value = "key", name = "index_history_key", unique = true)})
public class HistoryKey implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "key")
    public String key;

    @ColumnInfo(name = "date")
    public long date;

    public HistoryKey() {
    }

    @Ignore
    public HistoryKey(int id, String key, long date) {
        this.id = id;
        this.key = key;
        this.date = date;
    }

    @Ignore
    protected HistoryKey(Parcel in) {
        id = in.readInt();
        key = in.readString();
        date = in.readLong();
    }

    public static final Creator<HistoryKey> CREATOR = new Creator<HistoryKey>() {
        @Override
        public HistoryKey createFromParcel(Parcel in) {
            return new HistoryKey(in);
        }

        @Override
        public HistoryKey[] newArray(int size) {
            return new HistoryKey[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(key);
        parcel.writeLong(date);
    }
}
