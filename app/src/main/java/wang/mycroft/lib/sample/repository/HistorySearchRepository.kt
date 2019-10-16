package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val historySearchList: LiveData<List<HistoryKey>> = historyKeyService.getAllHistoryKey()

    fun loadHotKeyList() {

        viewModelScope.launch {
            try {
                hotKeyListLiveData.value = withContext(Dispatchers.IO) {
                    NetService.getHotKeyList()
                }
            } catch (e: Exception) {
                hotKeyListLiveData.value = NetModel.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}