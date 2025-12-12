package com.epam.deltix.util.lang;

import com.epam.deltix.util.lang.Disposable;

public interface DisposableListener<T extends Disposable> {
    
    void disposed(T resource);
}
