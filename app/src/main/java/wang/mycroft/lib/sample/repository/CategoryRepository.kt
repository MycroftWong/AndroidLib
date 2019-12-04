package wang.mycroft.lib.sample.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.WebService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class CategoryRepository : ViewModel() {

    private val categoryListLiveData = MutableLiveData<NetModel<List<Category>>>()

    val categoryList: LiveData<ResultModel<List<Category>>> =
        Transformations.map(categoryListLiveData) {
            SimpleResultModel<List<Category>>(it)
        }

    fun loadData() {

        viewModelScope.launch {
            try {
                categoryListLiveData.value = withContext(Dispatchers.IO) {
                    WebService.getCategoryList()
                }
            } catch (e: Exception) {
                categoryListLiveData.value = NetModel.error(e)
            }
        }
    }
}