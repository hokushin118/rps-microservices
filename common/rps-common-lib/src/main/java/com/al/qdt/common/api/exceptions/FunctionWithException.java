package com.al.qdt.common.api.exceptions;

@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
    R apply(T t) throws E;
}
