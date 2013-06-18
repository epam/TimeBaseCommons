package deltix.util.concurrent;

public interface CallableWithArgument<V,E> {
    V call (E argument) throws Exception;
}
