package wang.mycroft.lib.sample.repository.model

/**
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月13日
 * @author: wangqiang
 */
interface ResultModel<T> {

    /**
     * 状态码，0表示无错误
     */
    val code: Int

    /**
     * 获取错误信息
     */
    val message: String?

    /**
     * 获取真实数据
     */
    val data: T?
}

/**
 * 正确的状态码
 */
const val CODE_SUCCESS = 0

/**
 * 网络错误的状态码
 */
const val CODE_NET_ERROR = Int.MIN_VALUE

/**
 * [ResultModel]的简单实现类
 */
data class ResultModelImpl<T>(
    override val code: Int,
    override val message: String?,
    override val data: T?
) : ResultModel<T>

