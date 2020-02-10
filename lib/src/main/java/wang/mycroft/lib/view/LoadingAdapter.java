package wang.mycroft.lib.view;

import android.view.View;

/**
 * 提供[View]用于显示当前的状态
 *
 * @author wangqiang
 */
public interface LoadingAdapter {

    /**
     * get view for current status
     *
     * @param holder      Holder
     * @param convertView The old view to reuse, if possible.
     * @param status      current status
     * @return status view to show. Maybe convertView for reuse.
     * @see LoadingHolder
     */
    View getView(LoadingHolder holder, View convertView, int status);

}
