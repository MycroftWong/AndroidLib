package wang.mycroft.lib.sample.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

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
        return 1;
    }

    public Category getCategory() {
        return category;
    }
}
