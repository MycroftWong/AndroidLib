package wang.mycroft.lib.sample.service;

import java.io.File;

/**
 * 文件管理
 *
 * @author wangqiang
 */
public interface IFileService {

    /**
     * 获取网络文件缓存目录
     *
     * @return 网络文件缓存目录
     */
    File getNetCacheDir();

}
