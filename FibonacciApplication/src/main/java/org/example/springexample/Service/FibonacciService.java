package org.example.springexample.Service;

public interface FibonacciService<T> {
    T getFibonacciValueByNumber(Long number);
}
