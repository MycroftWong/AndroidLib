package wang.mycroft.lib.sample.ui.view;

import android.view.View;

import wang.mycroft.lib.view.LoadingAdapter;
import wang.mycroft.lib.view.LoadingHolder;

/**
 * 状态view adapter
 *
 * @author wangqiang
 */
public class SpinLoadingAdapter implements LoadingAdapter {
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

