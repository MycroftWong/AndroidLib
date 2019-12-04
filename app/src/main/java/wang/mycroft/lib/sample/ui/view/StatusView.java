package wang.mycroft.lib.sample.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.SpinKitView;

import wang.mycroft.lib.sample.R;
import wang.mycroft.lib.view.Loading;

/**
 * @author mycroft
 */
@SuppressLint("ViewConstructor")
final class StatusView extends LinearLayout {

    private SpinKitView spinKitView;
    private TextView statusText;
    private TextView retryButton;

    public StatusView(@NonNull Context context, @NonNull Runnable retryTask) {
        super(context);
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.loading_status_view, this, true);
        spinKitView = findViewById(R.id.spinKitView);
        statusText = findViewById(R.id.statusText);
        retryButton = findViewById(R.id.retryButton);

        retryButton.setOnClickListener(v -> retryTask.run());
    }

    public void setStatus(int status) {
        switch (status) {
            case Loading.STATUS_LOAD_SUCCESS:
                setVisibility(GONE);
                onSuccess();
                break;
            case Loading.STATUS_LOADING:
                onLoading();
                break;
            case Loading.STATUS_LOAD_FAILED:
                onLoadingFailed();
                break;
            case Loading.STATUS_EMPTY_DATA:
                onEmpty();
                break;
            default:
                break;
        }
    }

    private void onEmpty() {
        // 2019/4/30 修改
        spinKitView.setVisibility(GONE);
        statusText.setVisibility(VISIBLE);
        retryButton.setVisibility(GONE);

        statusText.setText(R.string.text_load_empty);
        retryButton.setEnabled(false);
    }

    private void onLoadingFailed() {
        spinKitView.setVisibility(GONE);
        statusText.setVisibility(VISIBLE);
        retryButton.setVisibility(VISIBLE);

        statusText.setText(R.string.text_load_failed);
        retryButton.setEnabled(true);
    }

    private void onSuccess() {
        // nothing
    }

    private void onLoading() {
        spinKitView.setVisibility(VISIBLE);
        statusText.setVisibility(GONE);
        retryButton.setVisibility(GONE);

        statusText.setText(R.string.text_loading);
        retryButton.setEnabled(false);
    }
}
