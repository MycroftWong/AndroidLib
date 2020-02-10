package wang.mycroft.lib.sample.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Tools
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

class ToolsViewModel : ViewModel() {

    private val toolsListLiveData = MutableLiveData<NetModel<List<Tools>>>()

    val toolsList: LiveData<ResultModel<List<Tools>>> = Transformations.map(toolsListLiveData) {
        NetResultModel(it)
    }

    fun loadToolsList() {
        viewModelScope.launch {
            try {
                toolsListLiveData.value = withContext(Dispatchers.IO) {
                    WebService.getToolList()
                }
            } catch (throwable: Throwable) {
                toolsListLiveData.value = NetModel(CODE_NET_ERROR, throwable.netMessage(), null)
            }
        }
    }
}