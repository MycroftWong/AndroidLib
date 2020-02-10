package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.OfficialAccount
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

class OfficialAccountViewModel : ViewModel() {

    private val officialAccountListLiveData = MutableLiveData<NetModel<List<OfficialAccount>>>()

    val officialAccountList: LiveData<ResultModel<List<OfficialAccount>>> =
        Transformations.map(officialAccountListLiveData) { NetResultModel(it) }

    fun loadOfficialAccountList() {
        viewModelScope.launch {
            try {
                officialAccountListLiveData.value = withContext(Dispatchers.IO) {
                    WebService.getOfficialAccountList()
                }
            } catch (e: Exception) {
                officialAccountListLiveData.value = NetModel(CODE_NET_ERROR, e.netMessage(), null)
            }
        }
    }
}