package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Tools
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

class ToolsRepository : ViewModel() {

    private val toolsListLiveData = MutableLiveData<NetModel<List<Tools>>>()

    val toolsList: LiveData<ResultModel<List<Tools>>> = Transformations.map(toolsListLiveData) {
        SimpleResultModel(it)
    }

    fun loadToolsList() {
        viewModelScope.launch {
            try {
                toolsListLiveData.value = withContext(Dispatchers.IO) {
                    NetService.getToolList()
                }
            } catch (e: Exception) {
                toolsListLiveData.value = NetModel.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}