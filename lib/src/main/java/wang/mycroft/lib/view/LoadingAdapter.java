package wang.mycroft.lib.view;

import android.view.View;

/**
 * Provides view to show current loading status
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
