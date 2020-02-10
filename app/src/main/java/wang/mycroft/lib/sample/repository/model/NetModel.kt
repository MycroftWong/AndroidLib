package wang.mycroft.lib.sample.repository.model

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException
import wang.mycroft.lib.sample.BuildConfig
import wang.mycroft.lib.sample.R
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

/**
 * 统一的网络返回数据结构
 *
 * @author Mycroft Wong
 * @date 2020年01月17日
 */
data class NetModel<T>(
    /**
     * 状态码
     */
    @SerializedName("errorCode")
    val code: Int,
    /**
     * 错误信息
     */
    @SerializedName("errorMsg")
    val message: String?,
    /**
     * 网络返回的真实数据
     */
    @SerializedName("data")
    val data: T?
)

/**
 * 将网络访问过程中得到的[Throwable]转化成对应的网络错误提示文字
 *
 * @return 网络异常提示文字
 */
fun Throwable.netMessage(): String {
    if (BuildConfig.DEBUG) {
        LogUtils.e(this)
    }
    return when (this) {
        is SocketTimeoutException -> {
            StringUtils.getString(R.string.error_socket_timeout)
        }
        is HttpException -> {
            val httpException: HttpException = this
            return when {
                httpException.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> StringUtils.getString(
                    R.string.error_unauthorized
                )
                httpException.code() in 400..499 -> StringUtils.getString(R.string.error_client)
                httpException.code() in 500..599 -> StringUtils.getString(R.string.error_server)
                else -> StringUtils.getString(R.string.error_unknown)
            }
        }
        is ConnectException -> StringUtils.getString(R.string.error_connect)
        else -> StringUtils.getString(R.string.error_net_other)
    }
}