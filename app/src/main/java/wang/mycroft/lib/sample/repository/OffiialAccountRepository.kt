package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.OfficialAccount
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

class OffiialAccountRepository : ViewModel() {

    private val officialAccountListLiveData = MutableLiveData<NetModel<List<OfficialAccount>>>()

    val officialAccountList: LiveData<ResultModel<List<OfficialAccount>>> =
        Transformations.map(officialAccountListLiveData) {
            SimpleResultModel(it)
        }

    fun loadOfficialAccountList() {
        viewModelScope.launch {
            try {
                officialAccountListLiveData.value = withContext(Dispatchers.IO) {
                    NetService.getOfficialAccountList()
                }
            } catch (e: Exception) {
                officialAccountListLiveData.value = NetModel.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}