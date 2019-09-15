package wang.mycroft.lib.net;

/**
 * Created by Mycroft_Wong on 2015/12/30.
 */
public interface HttpConstants {

    String ACCEPT_CHARSET = "Accept-Charset";

    String USER_AGENT = "User-Agent";

    String ACCEPT_ENCODING = "Accept-Encoding";

    String CACHE_CONTROL = "Cache-Control";

    String NO_CACHE = "no-cache";

    String FORCE_CONTROL = "only-if-cached, max-stale=" + Integer.MAX_VALUE;

}
