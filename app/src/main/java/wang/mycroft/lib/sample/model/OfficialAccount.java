package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class OfficialAccount implements Parcelable {

    /**
     * children : []
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */

    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private boolean userControlSetTop;
    private int visible;
    private List<OfficialAccount> children;

    public OfficialAccount() {
    }

    protected OfficialAccount(Parcel in) {
        courseId = in.readInt();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        parentChapterId = in.readInt();
        userControlSetTop = in.readByte() != 0;
        visible = in.readInt();
        children = in.createTypedArrayList(OfficialAccount.CREATOR);
    }

    public static final Creator<OfficialAccount> CREATOR = new Creator<OfficialAccount>() {
        @Override
        public OfficialAccount createFromParcel(Parcel in) {
            return new OfficialAccount(in);
        }

        @Override
        public OfficialAccount[] newArray(int size) {
            return new OfficialAccount[size];
        }
    };

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public boolean isUserControlSetTop() {
        return userControlSetTop;
    }

    public void setUserControlSetTop(boolean userControlSetTop) {
        this.userControlSetTop = userControlSetTop;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<OfficialAccount> getChildren() {
        return children;
    }

    public void setChildren(List<OfficialAccount> children) {
        this.children = children;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(courseId);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(order);
        parcel.writeInt(parentChapterId);
        parcel.writeByte((byte) (userControlSetTop ? 1 : 0));
        parcel.writeInt(visible);
        parcel.writeTypedList(children);
    }
}
