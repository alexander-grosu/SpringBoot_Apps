package FibonacciCalculatorTestJUnit5;

import org.example.springexample.FibonacciCalculator.FibonacciCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Fibonacci calculator test")
public class FibonacciCalculatorTest {

    @Test
    @DisplayName("Calculate fibonacci value of numbers 0...9")
    void fibonacciCalculatorTest() {
        assertThat(FibonacciCalculator.calculateFibonacciValue(0L)).isEqualTo(0L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(1L)).isEqualTo(1L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(2L)).isEqualTo(1L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(3L)).isEqualTo(2L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(4L)).isEqualTo(3L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(5L)).isEqualTo(5L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(6L)).isEqualTo(8L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(7L)).isEqualTo(13L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(8L)).isEqualTo(21L);
        assertThat(FibonacciCalculator.calculateFibonacciValue(9L)).isEqualTo(34L);
    }

    @Test
    @DisplayName("Fibonacci calculator exception test")
    void fibonacciCalculatorExceptionTest() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> FibonacciCalculator.calculateFibonacciValue(-1L));
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }
}
