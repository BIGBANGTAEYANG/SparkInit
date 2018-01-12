package app.exception;

/**
 * 状态异常类
 *
 * @author xd
 */
public class StatusException extends RuntimeException {

	private static final long serialVersionUID = -3263353935088745810L;
	private int code;

    public StatusException() {
        super();
    }

    public StatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusException(String message) {
        super(message);
    }

    public StatusException(Throwable cause) {
        super(cause);
    }

    public StatusException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
