package wang.mycroft.lib.sample.repository.model

/**
 * 带有数据是否已全部加载完成，无法上拉加载的标志
 * 用于网络分页动态加载
 */
data class PagedResult<T>(
    // 真是的数据
    val data: T,
    // true表示数据已全部加载，无法上拉加载
    val noMoreData: Boolean = false
)

/**
 * 结合[PagedResult]的[ResultModel]
 */
data class PagedResultModel<T>(
    override val code: Int,
    override val message: String?,
    override val data: PagedResult<T>?
) : ResultModel<PagedResult<T>>