package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import wang.mycroft.lib.sample.model.Project
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

class ProjectRepository : ViewModel() {

    private val projectListLiveData = MutableLiveData<NetModel<List<Project>>>()

    val projectList: LiveData<ResultModel<List<Project>>> =
        Transformations.map(projectListLiveData) { SimpleResultModel(it) }

    fun loadProjectList() {
        viewModelScope.launch {
            try {
                projectListLiveData.value = NetService.getProjectList()
            } catch (e: Exception) {
                projectListLiveData.value = NetModel.error(e)
            }
        }
    }
}