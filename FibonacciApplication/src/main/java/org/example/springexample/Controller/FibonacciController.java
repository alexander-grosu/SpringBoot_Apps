package org.example.springexample.Controller;

import org.example.springexample.Service.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/fibonacci")
public class FibonacciController {

    @Autowired
    private final FibonacciService<Long> fibonacciService;

    public FibonacciController(FibonacciService<Long> fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    /*
    * Postman
    * Method GET
    * http://localhost:8080/api/fibonacci/{number}
    * */
    @GetMapping("/{number}")
    public ResponseEntity<Long> getFibonacciValue(@PathVariable long number){
        if(number < 0 || String.valueOf(number).isBlank() || !String.valueOf(number).matches("\\d+")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        long fibonacciValue = fibonacciService.getFibonacciValueByNumber(number);
        return ResponseEntity.status(HttpStatus.OK).body(fibonacciValue);
    }
}
