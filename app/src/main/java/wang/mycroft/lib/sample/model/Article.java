package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article implements Parcelable {

    /**
     * apkLink :
     * author : 老邢Thierry
     * chapterId : 169
     * chapterName : gradle
     * collect : false
     * courseId : 13
     * desc :
     * envelopePic :
     * fresh : true
     * id : 8696
     * link : https://www.jianshu.com/p/0624a18cf8fa
     * niceDate : 14小时前
     * origin :
     * prefix :
     * projectLink :
     * publishTime : 1563207294000
     * superChapterId : 60
     * superChapterName : 开发环境
     * tags : []
     * title : 知乎 Android Gradle plugin 实践
     * type : 0
     * userId : -1
     * visible : 1
     * zan : 0
     */

    private String apkLink;
    private String author;
    private int chapterId;
    private String chapterName;
    private boolean collect;
    private int courseId;
    private String desc;
    private String envelopePic;
    private boolean fresh;
    private int id;
    private String link;
    private String niceDate;
    private String origin;
    private String prefix;
    private String projectLink;
    private long publishTime;
    private int superChapterId;
    private String superChapterName;
    private String title;
    private int type;
    private int userId;
    private int visible;
    private int zan;
    private List<ArticleTag> tags;
    /**
     * audit : 1
     * niceShareDate : 16小时前
     * selfVisible : 0
     * shareDate : 1570637323000
     * shareUser : 鸿洋
     * tags : []
     */

    @SerializedName("audit")
    private int audit;
    @SerializedName("niceShareDate")
    private String niceShareDate;
    @SerializedName("selfVisible")
    private int selfVisible;
    @SerializedName("shareDate")
    private long shareDate;
    @SerializedName("shareUser")
    private String shareUser;

    public Article() {
    }

    protected Article(Parcel in) {
        apkLink = in.readString();
        author = in.readString();
        chapterId = in.readInt();
        chapterName = in.readString();
        collect = in.readByte() != 0;
        courseId = in.readInt();
        desc = in.readString();
        envelopePic = in.readString();
        fresh = in.readByte() != 0;
        id = in.readInt();
        link = in.readString();
        niceDate = in.readString();
        origin = in.readString();
        prefix = in.readString();
        projectLink = in.readString();
        publishTime = in.readLong();
        superChapterId = in.readInt();
        superChapterName = in.readString();
        title = in.readString();
        type = in.readInt();
        userId = in.readInt();
        visible = in.readInt();
        zan = in.readInt();
        tags = in.createTypedArrayList(ArticleTag.CREATOR);
        audit = in.readInt();
        niceShareDate = in.readString();
        selfVisible = in.readInt();
        shareDate = in.readLong();
        shareUser = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getApkLink() {
        return apkLink;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(int superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public String getNiceShareDate() {
        return niceShareDate;
    }

    public void setNiceShareDate(String niceShareDate) {
        this.niceShareDate = niceShareDate;
    }

    public int getSelfVisible() {
        return selfVisible;
    }

    public void setSelfVisible(int selfVisible) {
        this.selfVisible = selfVisible;
    }

    public long getShareDate() {
        return shareDate;
    }

    public void setShareDate(long shareDate) {
        this.shareDate = shareDate;
    }

    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public List<ArticleTag> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTag> tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apkLink);
        dest.writeString(author);
        dest.writeInt(chapterId);
        dest.writeString(chapterName);
        dest.writeByte((byte) (collect ? 1 : 0));
        dest.writeInt(courseId);
        dest.writeString(desc);
        dest.writeString(envelopePic);
        dest.writeByte((byte) (fresh ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(link);
        dest.writeString(niceDate);
        dest.writeString(origin);
        dest.writeString(prefix);
        dest.writeString(projectLink);
        dest.writeLong(publishTime);
        dest.writeInt(superChapterId);
        dest.writeString(superChapterName);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeInt(userId);
        dest.writeInt(visible);
        dest.writeInt(zan);
        dest.writeTypedList(tags);
        dest.writeInt(audit);
        dest.writeString(niceShareDate);
        dest.writeInt(selfVisible);
        dest.writeLong(shareDate);
        dest.writeString(shareUser);
    }
}
