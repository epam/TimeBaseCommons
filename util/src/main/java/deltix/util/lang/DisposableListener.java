package deltix.util.lang;

public interface DisposableListener<T extends Disposable> {
    
    void disposed(T resource);
}
