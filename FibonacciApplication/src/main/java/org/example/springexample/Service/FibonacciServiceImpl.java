package org.example.springexample.Service;

import lombok.extern.log4j.Log4j;
import org.example.springexample.Entity.Fibonacci;
import org.example.springexample.FibonacciCalculator.FibonacciCalculator;
import org.example.springexample.Repository.FibonacciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FibonacciServiceImpl implements FibonacciService<Long>{
    @Autowired
    private final FibonacciRepository fibonacciRepository;

    public FibonacciServiceImpl(FibonacciRepository fibonacciRepository) {
        this.fibonacciRepository = fibonacciRepository;
    }

    @Override
    public Long getFibonacciValueByNumber(Long number) {

        long fibonacciVal = FibonacciCalculator.calculateFibonacciValue(number);
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.setNumber(number);
        fibonacci.setFibonacci_value(fibonacciVal);

        if( fibonacciRepository.count() == 0 || fibonacciRepository.findByNumber(number)
                .stream()
                .noneMatch(o -> o.getNumber().equals(number))){
            fibonacciRepository.save(fibonacci);
            return fibonacciVal;
        }
            return fibonacciRepository.findByNumber(number)
                    .stream()
                    .filter(o -> o.getNumber().equals(number))
                    .findFirst()
                    .orElseThrow()
                    .getFibonacci_value();
    }
}
