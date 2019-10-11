package wang.mycroft.lib.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import wang.mycroft.lib.sample.model.HistoryKey
import wang.mycroft.lib.sample.model.HotKey
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel
import wang.mycroft.lib.sample.service.HistoryKeyServiceImpl
import wang.mycroft.lib.sample.service.IHistoryKeyService

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月16
 * @author: wangqiang
 */
class HistorySearchRepository : ViewModel() {

    private val hotKeyListLiveData = MutableLiveData<NetModel<List<HotKey>>>()

    val hotKeyList: LiveData<ResultModel<List<HotKey>>> = Transformations.map(hotKeyListLiveData) {
        SimpleResultModel(it)
    }

    private val historyKeyService: IHistoryKeyService = HistoryKeyServiceImpl

    private val historySearchListLiveData = MutableLiveData<List<HistoryKey>>()

    val historySearchList: LiveData<List<HistoryKey>> =
        Transformations.map(historySearchListLiveData) {
            it
        }

    private var disposable: Disposable? = null

    private var hotKeyDisposable: Disposable? = null

    fun loadHotKeyList() {
        if (hotKeyDisposable != null) {
            return
        }

        hotKeyDisposable = NetService.getInstance().hotKeyList
            .subscribe({ hotKeys ->
                hotKeyListLiveData.value = hotKeys
            }, { throwable ->
                hotKeyDisposable = null
                hotKeyListLiveData.value = NetModel<List<HotKey>>(-1, throwable.message, null)
            }, { hotKeyDisposable = null })
    }

    fun loadHistorySearchList() {
        disposable = historyKeyService
            .allHistoryKey
            .subscribe({ historyKeys ->
                historySearchListLiveData.value = historyKeys
            }, {
                disposable = null
                historySearchListLiveData.value = listOf()
            }, { disposable = null })
    }
}