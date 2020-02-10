package wang.mycroft.lib.sample.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import wang.mycroft.lib.sample.ui.adapter.recycler.ToolsAdapter;

/**
 * @author mycroft
 */
public final class ToolsContent implements MultiItemEntity {

    private final Article article;

    public ToolsContent(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return ToolsAdapter.TYPE_CONTENT;
    }
}
