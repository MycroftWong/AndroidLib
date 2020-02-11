package wang.mycroft.lib.sample.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Navigation
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.*

class NavigationViewModel : ViewModel() {

    private val navigationListLiveDataInternal = MutableLiveData<NetModel<List<Navigation>>>()

    val navigationListLiveData: LiveData<ResultModel<List<Navigation>>> =
        Transformations.map(navigationListLiveDataInternal) {
            NetResultModel(it)
        }

    fun loadNavigationList() {
        viewModelScope.launch {
            try {
                navigationListLiveDataInternal.value = withContext(Dispatchers.IO) {
                    WebService.getNavigationList()
                }
            } catch (throwable: Throwable) {
                navigationListLiveDataInternal.value =
                    NetModel(CODE_NET_ERROR, throwable.netMessage(), null)
            }
        }
    }
}