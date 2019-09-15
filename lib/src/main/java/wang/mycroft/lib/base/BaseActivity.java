package wang.mycroft.lib.base;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import wang.mycroft.lib.R;


/**
 * Created by Mycroft_Wong on 2015/12/30.
 *
 * @author mycroft
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialogHelper loadingDialogHelper;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        initFields(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        loadingDialogHelper = new LoadingDialogHelper(this, this::createLoadingDialog);
        initViews();
        loadData();
    }

    /**
     * 获取布局资源id
     *
     * @return layout id
     */
    protected abstract int getResId();

    /**
     * 进行初始化工作，在super.onCreate(Bundle)之前调用
     *
     * @param savedInstanceState 保存的状态
     */
    protected abstract void initFields(@Nullable Bundle savedInstanceState);

    /**
     * 初始化view, 在{@link android.app.Activity#setContentView(int)}之后调用
     */
    protected abstract void initViews();

    /**
     * 加载数据，在所有初始化完成之后调用
     */
    protected abstract void loadData();

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
     * 隐藏加载Dialog
     */
    protected final void hideLoadingDialog() {
        loadingDialogHelper.hideLoadingDialog();
    }

    /**
     * 构造通用的加载{@link Dialog}, 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    @NonNull
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(this, R.style.LoadingDialog);
        dialog.setContentView(R.layout.common_dialog);
        return dialog;
    }

}

