package wang.mycroft.lib.base;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import wang.mycroft.lib.R;

/**
 * @author Mycroft
 * @date 2016/6/13
 */
public abstract class BaseFragment extends Fragment {

    private LoadingDialogHelper loadingDialogHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialogHelper = new LoadingDialogHelper(this, this::createLoadingDialog);
    }

    /**
     * 显示通用的加载{@link Dialog}, 默认cancelable=false
     */
    protected final void showLoadingDialog() {
        loadingDialogHelper.showLoadingDialog();
    }

    /**
     * 显示通用的加载{@link Dialog}
     *
     * @param cancelable 是否允许点击空白处取消
     */
    protected final void showLoadingDialog(boolean cancelable) {
        loadingDialogHelper.showLoadingDialog(cancelable);
    }

    /**
     * 构造通用的加载{@link Dialog}, 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    @NonNull
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.LoadingDialog);
        dialog.setContentView(R.layout.common_dialog);
        return dialog;
    }

    /**
     * 隐藏加载Dialog
     */
    protected final void hideLoadingDialog() {
        loadingDialogHelper.hideLoadingDialog();
    }
}
