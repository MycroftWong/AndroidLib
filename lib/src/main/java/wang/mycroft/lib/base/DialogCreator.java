package wang.mycroft.lib.base;

import android.app.Dialog;

/**
 * LoadingDialog 构造器
 *
 * @author Mycroft Wong
 * @date 2019年11月15日
 */
public interface DialogCreator {

    /**
     * create loading dialog
     *
     * @return LoadingDialog
     */
    Dialog createLoadingDialog();
}
