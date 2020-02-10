package wang.mycroft.lib.view;

import android.view.View;

/**
 * 统一加载adapter
 *
 * @author mycroft
 */
public class DefaultLoadingAdapter implements LoadingAdapter {
    @Override
    public View getView(LoadingHolder holder, View convertView, int status) {

        StatusView statusView = null;

        if (convertView instanceof StatusView) {
            statusView = (StatusView) convertView;
        }

        if (statusView == null) {
            statusView = new StatusView(holder.getContext(), holder.getRetryTask());
        }

        statusView.setStatus(status);

        return statusView;
    }
}
