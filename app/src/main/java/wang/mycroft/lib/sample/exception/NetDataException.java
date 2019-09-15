package wang.mycroft.lib.sample.exception;

public class NetDataException extends Exception {

    private NetDataException(String errorMsg) {
        super(errorMsg);
    }

    public static NetDataException newInstance(String errorMsg) {
        return new NetDataException(errorMsg);
    }
}
