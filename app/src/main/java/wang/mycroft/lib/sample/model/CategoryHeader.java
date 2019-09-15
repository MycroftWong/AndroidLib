package wang.mycroft.lib.sample.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import wang.mycroft.lib.sample.ui.adapter.recycler.CategoryAdapter;

/**
 * 将Category分组
 *
 * @author mycroft
 */
public final class CategoryHeader extends AbstractExpandableItem<CategoryContent> implements MultiItemEntity {

    @Override
    public int getLevel() {
        return 0;
    }

    private final Category category;

    public CategoryHeader(Category category) {
        this.category = category;
        for (Category item : category.getChildren()) {
            addSubItem(new CategoryContent(item));
        }
    }

    @Override
    public int getItemType() {
        return CategoryAdapter.TYPE_HEADER;
    }

    public Category getCategory() {
        return category;
    }
}
