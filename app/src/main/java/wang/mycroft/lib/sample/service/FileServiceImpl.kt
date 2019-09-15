package wang.mycroft.lib.sample.service

import com.blankj.utilcode.util.Utils
import java.io.File

/**
 *
 * @blog: https://blog.mycroft.wang
 * @date: 2019年09月15
 * @author: wangqiang
 */
object FileServiceImpl : IFileService {

    private const val DIR_NET_CACHE = "net"

    override fun getNetCacheDir(): File {
        return File(Utils.getApp().cacheDir, DIR_NET_CACHE)
    }
}