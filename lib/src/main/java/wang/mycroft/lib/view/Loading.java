package wang.mycroft.lib.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * manage loading status view<br>
 * usage:<br>
 * //if set true, logs will print into logcat<br>
 * Gloading.debug(trueOrFalse);<br>
 * //init the default loading status view creator ({@link LoadingAdapter})<br>
 * Gloading.initDefault(adapter);<br>
 * //wrap an activity. return the holder<br>
 * Holder holder = Gloading.getDefault().wrap(activity);<br>
 * //wrap an activity and set retry task. return the holder<br>
 * Holder holder = Gloading.getDefault().wrap(activity).withRetry(retryTask);<br>
 * <br>
 * holder.showLoading() //show loading status view by holder<br>
 * holder.showLoadSuccess() //show load success status view by holder (frequently, hide gloading)<br>
 * holder.showFailed() //show load failed status view by holder (frequently, needs retry task)<br>
 * holder.showEmpty() //show empty status view by holder. (load completed, but data is empty)
 *
 * @author wangqiang
 */
public final class Loading {

    public static final boolean DEBUG = false;

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;

    private static volatile Loading loading;
    private LoadingAdapter loadingAdapter;

    private Loading() {
    }

    /**
     * Create a new Loading different from the default one
     *
     * @param adapter another adapter different from the default one
     * @return Loading
     */
    public static Loading from(LoadingAdapter adapter) {
        Loading loading = new Loading();
        loading.loadingAdapter = adapter;
        return loading;
    }

    /**
     * get default Loading object for global usage in whole app
     *
     * @return default Loading object
     */
    public static Loading getDefault() {
        if (loading == null) {
            synchronized (Loading.class) {
                if (loading == null) {
                    loading = new Loading();
                }
            }
        }
        return loading;
    }

    /**
     * init the default loading status view creator ({@link LoadingAdapter})
     *
     * @param adapter adapter to create all status views
     */
    public static void initDefault(LoadingAdapter adapter) {
        getDefault().loadingAdapter = adapter;
    }

    /**
     * Loading(loading status view) wrap the whole activity
     * wrapper is android.R.id.content
     *
     * @param activity current activity object
     * @return holder of Loading
     */
    public LoadingHolder wrap(Activity activity) {
        ViewGroup wrapper = activity.findViewById(android.R.id.content);
        return new LoadingHolder(loadingAdapter, activity, wrapper);
    }

    /**
     * Loading(loading status view) wrap the specific view.
     *
     * @param view view to be wrapped
     * @return Holder
     */
    public LoadingHolder wrap(View view) {
        FrameLayout wrapper = new FrameLayout(view.getContext());
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            wrapper.setLayoutParams(lp);
        }
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view);
            parent.removeView(view);
            parent.addView(wrapper, index);
        }
        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        wrapper.addView(view, newLp);
        return new LoadingHolder(loadingAdapter, view.getContext(), wrapper);
    }
}