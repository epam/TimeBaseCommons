package deltix.qsrv.hf.pub;

public class InstrumentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InstrumentNotFoundException(String message) {
        super(message);
    }

    public InstrumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
