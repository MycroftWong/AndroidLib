package wang.mycroft.lib.view;

import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import wang.mycroft.lib.BuildConfig;

/**
 * @author wangqiang
 */
public final class LoadingHolder {

    private LoadingAdapter loadingAdapter;
    private Context context;
    private Runnable retryTask;
    private View curStatusView;
    private ViewGroup wrapper;
    private int curState;
    private SparseArray<View> statusViews = new SparseArray<>(4);
    private Object data;

    protected LoadingHolder(LoadingAdapter adapter, Context context, ViewGroup wrapper) {
        this.loadingAdapter = adapter;
        this.context = context;
        this.wrapper = wrapper;
    }

    /**
     * set retry task when user click the retry button in load failed page
     *
     * @param task when user click in load failed UI, run this task
     * @return this
     */
    public LoadingHolder withRetry(Runnable task) {
        retryTask = task;
        return this;
    }

    /**
     * set extension data
     *
     * @param data extension data
     * @return this
     */
    public LoadingHolder withData(Object data) {
        this.data = data;
        return this;
    }

    public void showLoading() {
        showLoadingStatus(Loading.STATUS_LOADING);
    }

    public void showLoadSuccess() {
        showLoadingStatus(Loading.STATUS_LOAD_SUCCESS);
    }

    public void showLoadFailed() {
        showLoadingStatus(Loading.STATUS_LOAD_FAILED);
    }

    public void showEmpty() {
        showLoadingStatus(Loading.STATUS_EMPTY_DATA);
    }

    /**
     * Show specific status UI
     *
     * @param status status
     * @see #showLoading()
     * @see #showLoadFailed()
     * @see #showLoadSuccess()
     * @see #showEmpty()
     */
    public void showLoadingStatus(int status) {
        if (curState == status || !validate()) {
            return;
        }
        curState = status;
        //first try to reuse status view
        View convertView = statusViews.get(status);
        if (convertView == null) {
            //secondly try to reuse current status view
            convertView = curStatusView;
        }
        try {
            //call customer adapter to get UI for specific status. convertView can be reused
            View view = loadingAdapter.getView(this, convertView, status);
            if (view == null) {
                LogUtils.e(loadingAdapter.getClass().getName() + ".getView returns null");
                return;
            }
            if (view != curStatusView || wrapper.indexOfChild(view) < 0) {
                if (curStatusView != null) {
                    wrapper.removeView(curStatusView);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setElevation(Float.MAX_VALUE);
                }
                wrapper.addView(view);
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp != null) {
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            } else if (wrapper.indexOfChild(view) != wrapper.getChildCount() - 1) {
                // make sure loading status view at the front
                view.bringToFront();
            }
            curStatusView = view;
            statusViews.put(status, view);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        if (loadingAdapter == null) {
            LogUtils.i("Loading.Adapter is not specified.");
        }
        if (context == null) {
            LogUtils.i("Context is null.");
        }
        if (wrapper == null) {
            LogUtils.i("The wrapper of loading status view is null.");
        }
        return loadingAdapter != null && context != null && wrapper != null;
    }

    public Context getContext() {
        return context;
    }

    /**
     * get wrapper
     *
     * @return container of gloading
     */
    public ViewGroup getWrapper() {
        return wrapper;
    }

    /**
     * get retry task
     *
     * @return retry task
     */
    public Runnable getRetryTask() {
        return retryTask;
    }

    /**
     * get extension data
     *
     * @param <T> return type
     * @return data
     */
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        try {
            return (T) data;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                LogUtils.i(e);
            }
        }
        return null;
    }
}
