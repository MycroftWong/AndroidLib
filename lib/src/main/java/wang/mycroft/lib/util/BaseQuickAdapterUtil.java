package wang.mycroft.lib.util;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.lang.reflect.Field;

/**
 * 用于清理{@link BaseQuickAdapter}引用的{@link androidx.recyclerview.widget.RecyclerView}实例，避免内存泄漏
 * Usage:
 * 在{@link Activity#onDestroy()} 或 {@link Fragment#onDestroyView()}中使用
 *
 * @author mycroft
 */
public final class BaseQuickAdapterUtil {
    private BaseQuickAdapterUtil() {
    }

    public static void releaseAdapter(BaseQuickAdapter adapter) {
        if (null == adapter) {
            return;
        }
        try {
            Field field = BaseQuickAdapter.class.getDeclaredField("mRecyclerView");
            field.setAccessible(true);
            field.set(adapter, null);
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }
}
