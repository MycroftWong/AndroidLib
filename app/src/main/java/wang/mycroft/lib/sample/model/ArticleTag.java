package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class ArticleTag implements Parcelable {
    /**
     * name : 问答
     * url : /article/list/0?cid=440
     */

    private String name;
    private String url;

    public ArticleTag() {
    }

    protected ArticleTag(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<ArticleTag> CREATOR = new Creator<ArticleTag>() {
        @Override
        public ArticleTag createFromParcel(Parcel in) {
            return new ArticleTag(in);
        }

        @Override
        public ArticleTag[] newArray(int size) {
            return new ArticleTag[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
    }
}
