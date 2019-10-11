package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 首页banner
 *
 * @author Mycroft Wong
 * @date 2019年10月11日
 */
public final class Banner implements Parcelable {

    /**
     * desc : Android高级进阶直播课免费学习
     * id : 23
     * imagePath : https://wanandroid.com/blogimgs/92d96db5-d951-4223-ac42-e13a62899f50.jpeg
     * isVisible : 1
     * order : 0
     * title : Android高级进阶直播课免费学习
     * type : 0
     * url : https://url.163.com/4bj
     */

    @SerializedName("desc")
    private String desc;
    @SerializedName("id")
    private int id;
    @SerializedName("imagePath")
    private String imagePath;
    @SerializedName("isVisible")
    private int isVisible;
    @SerializedName("order")
    private int order;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private int type;
    @SerializedName("url")
    private String url;

    public Banner() {
    }

    protected Banner(Parcel in) {
        desc = in.readString();
        id = in.readInt();
        imagePath = in.readString();
        isVisible = in.readInt();
        order = in.readInt();
        title = in.readString();
        type = in.readInt();
        url = in.readString();
    }

    public static final Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeInt(isVisible);
        dest.writeInt(order);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(url);
    }
}
