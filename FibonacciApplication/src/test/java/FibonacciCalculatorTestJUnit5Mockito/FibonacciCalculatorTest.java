package FibonacciCalculatorTestJUnit5Mockito;

import org.example.springexample.Service.FibonacciService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Fibonacci calculator test JUnit5 Mockito")
class FibonacciCalculatorTest {
    @Mock
    private FibonacciService<Long> fibonacciService;

    @Test
    @DisplayName("Fibonacci calculator test")
    void fibonacciCalculatorTest() {
        when(fibonacciService.getFibonacciValueByNumber(10L)).thenReturn(55L);
        long actualValue = fibonacciService.getFibonacciValueByNumber(10L);
        assertEquals(55, actualValue);
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(10L);

        when(fibonacciService.getFibonacciValueByNumber(1L)).thenReturn(1L);
        long actualFromOne = fibonacciService.getFibonacciValueByNumber(1L);
        assertEquals(1, actualFromOne);
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(1L);

        when(fibonacciService.getFibonacciValueByNumber(2L)).thenReturn(1L);
        long actualFromTwo = fibonacciService.getFibonacciValueByNumber(2L);
        assertEquals(1, actualFromTwo);
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(2L);

        when(fibonacciService.getFibonacciValueByNumber(0L)).thenReturn(0L);
        long actualFromZero = fibonacciService.getFibonacciValueByNumber(0L);
        assertEquals(0, actualFromZero);
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(0L);
    }

    @Test
    @DisplayName("Fibonacci calculator exception test")
    void fibonacciCalculatorExceptionTest() {
        // 1
        when(fibonacciService.getFibonacciValueByNumber(-1L)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> fibonacciService.getFibonacciValueByNumber(-1L));
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(-1L);
        // 2
        when(fibonacciService.getFibonacciValueByNumber(-150L)).thenThrow(new IllegalArgumentException("number should be >= zero"));
        try {
            fibonacciService.getFibonacciValueByNumber(-150L);
            fail("Expected IllegalArgumentException to be thrown ");
        } catch (IllegalArgumentException e) {
            assertEquals("number should be >= zero", e.getMessage());
        }
        verify(fibonacciService, times(1)).getFibonacciValueByNumber(-150L);
    }
}
