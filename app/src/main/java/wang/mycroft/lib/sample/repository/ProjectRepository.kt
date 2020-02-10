package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Project
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

class ProjectRepository : ViewModel() {

    private val projectListLiveData = MutableLiveData<NetModel<List<Project>>>()

    val projectList: LiveData<ResultModel<List<Project>>> =
        Transformations.map(projectListLiveData) { NetResultModel(it) }

    fun loadProjectList() {
        viewModelScope.launch {
            try {
                projectListLiveData.value = withContext(Dispatchers.IO) {
                    WebService.getProjectList()
                }
            } catch (throwable: Throwable) {
                projectListLiveData.value = NetModel(CODE_NET_ERROR, throwable.netMessage(), null)
            }
        }
    }
}