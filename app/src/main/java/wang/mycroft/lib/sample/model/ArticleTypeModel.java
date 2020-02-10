package wang.mycroft.lib.sample.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 文章分类数据：有图片和无图片分开
 *
 * @author mycroft
 */
public class ArticleTypeModel implements MultiItemEntity, Parcelable {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    private final Article article;

    public ArticleTypeModel(Article article) {
        this.article = article;
    }

    protected ArticleTypeModel(Parcel in) {
        article = in.readParcelable(Article.class.getClassLoader());
    }

    public static final Creator<ArticleTypeModel> CREATOR = new Creator<ArticleTypeModel>() {
        @Override
        public ArticleTypeModel createFromParcel(Parcel in) {
            return new ArticleTypeModel(in);
        }

        @Override
        public ArticleTypeModel[] newArray(int size) {
            return new ArticleTypeModel[size];
        }
    };

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return TextUtils.isEmpty(article.getEnvelopePic()) ? TYPE_TEXT : TYPE_IMAGE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(article, i);
    }
}
