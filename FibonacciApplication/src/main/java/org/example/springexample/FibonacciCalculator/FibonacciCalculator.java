package org.example.springexample.FibonacciCalculator;

public class FibonacciCalculator {
    public static Long calculateFibonacciValue(Long n) {
        if (n == 0 || n == 1) {
            return n;
        } else if (n > 1) {
            long n0 = 0, n1 = 1;
            long temp;
            for (long i = 2; i <= n; i++) {
                temp = n0 + n1;
                n0 = n1;
                n1 = temp;
            }
            return n1;
        } else {
            throw new IllegalArgumentException("number should be >= zero");
        }
    }
}
