package org.example.springexample;

import org.example.springexample.Controller.FibonacciController;
import org.example.springexample.Service.FibonacciService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FibonacciController.class)
@DisplayName("MockMvc controller test")
public class MockMvcSControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FibonacciService<Long> fibonacciService;

    @Test
    @DisplayName("Controller test status 200 OK")
    void controllerTestStatus_200_OK() throws Exception {
        when(fibonacciService.getFibonacciValueByNumber(10L)).thenReturn(55L);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fibonacci/{number}", 10L))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value(55L));
    }

    @Test
    @DisplayName("Controller test status 400 BadRequest")
    void controllerTestStatus_400_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fibonacci/{number}", -1L))
                .andExpect(status().is4xxClientError());
    }
}
