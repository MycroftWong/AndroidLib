package wang.mycroft.lib.sample.repository.model

/**
 * 网络返回的[ResultModel]
 *
 * @author Mycroft Wong
 * @date 2020年01月17日
 */
class NetResultModel<T>(private val netModel: NetModel<T>) : ResultModel<T> {
    override val code: Int
        get() = netModel.code
    override val message: String?
        get() = netModel.message
    override val data: T?
        get() = netModel.data
}