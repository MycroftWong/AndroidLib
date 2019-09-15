package wang.mycroft.lib.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import wang.mycroft.lib.sample.model.Category
import wang.mycroft.lib.sample.net.NetModel
import wang.mycroft.lib.sample.net.NetService
import wang.mycroft.lib.sample.repository.model.ResultModel
import wang.mycroft.lib.sample.repository.model.SimpleResultModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class  CategoryRepository : ViewModel() {

    private var disposable: Disposable? = null

    private val categoryListLiveData = MutableLiveData<NetModel<List<Category>>>()

    val categoryList: LiveData<ResultModel<List<Category>>> =
        Transformations.map(categoryListLiveData) {
            SimpleResultModel<List<Category>>(it)
        }

    fun loadData() {
        if (disposable != null) {
            return
        }

        disposable = NetService.getInstance().categoryList2
            .subscribe({ categoryListNetModel ->
                categoryListLiveData.value = categoryListNetModel
            }, { throwable ->
                disposable = null
                categoryListLiveData.value = NetModel<List<Category>>(-1, throwable.message, null)
            }, { disposable = null })
    }
}