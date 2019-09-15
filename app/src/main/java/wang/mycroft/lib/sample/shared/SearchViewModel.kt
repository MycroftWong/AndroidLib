package wang.mycroft.lib.sample.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
class SearchViewModel : ViewModel() {

    val searchKey = MutableLiveData<String>()

    fun getSearchKey(): LiveData<String> {
        return searchKey
    }

    fun setSearchKey(searchKey: String) {
        this.searchKey.value = searchKey
    }
}