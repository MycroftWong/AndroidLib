package wang.mycroft.lib.sample.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import wang.mycroft.lib.sample.ui.adapter.recycler.CategoryAdapter;

public final class CategoryContent implements MultiItemEntity {

    private final Category category;

    public CategoryContent(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int getItemType() {
        return CategoryAdapter.TYPE_CONTENT;
    }
}
