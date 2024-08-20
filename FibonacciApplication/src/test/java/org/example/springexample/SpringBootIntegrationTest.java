package org.example.springexample;

import org.example.springexample.Entity.Fibonacci;
import org.example.springexample.Repository.FibonacciRepository;
import org.example.springexample.Service.FibonacciService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Application integration test")
public class SpringBootIntegrationTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private FibonacciRepository fibonacciRepository;
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    @Autowired
    private FibonacciService<Long> fibonacciService;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void fillingDataBase() {
        Fibonacci f = new Fibonacci();
        f.setId(1L);
        f.setNumber(10L);
        f.setFibonacci_value(55L);
        fibonacciRepository.saveIfNotExist(f);
    }

    @AfterEach
    void clearDB() {
        fibonacciRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Fibonacci mvc test")
    public void fibonacciMvcTest() {
        ResponseEntity<Long> responseOk = restTemplate.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/api/fibonacci/10", Long.class);
        //http status 200 ok
        assertTrue(responseOk.getStatusCode().is2xxSuccessful());
        assertEquals(55L, responseOk.getBody());

        ResponseEntity<Long> responseBadRequest = restTemplate.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/api/fibonacci/-1", Long.class);
        //http status 400 bad request
        assertTrue(responseBadRequest.getStatusCode().is4xxClientError());
        assertNull(responseBadRequest.getBody());
    }

    @Test
    @Order(2)
    @DisplayName("Fibonacci repository test")
    public void fibonacciRepositoryTest() {
        List<Fibonacci> list = jdbcTemplate
                .query("SELECT * FROM fibonacci", (resultSet, rowsNumber) -> {
                            Fibonacci fibonacci = new Fibonacci();
                            fibonacci.setId(resultSet.getLong("id"));
                            fibonacci.setNumber(resultSet.getLong("number"));
                            fibonacci.setFibonacci_value(resultSet.getLong("fibonacci_value"));
                            return fibonacci;
                        }
                );
        Fibonacci actual = list.get(0);
        Fibonacci expected = new Fibonacci();
        expected.setId(1L);
        expected.setNumber(10L);
        expected.setFibonacci_value(55L);

        assertThat(actual).isNotNull();
        assertThat(actual).isExactlyInstanceOf(Fibonacci.class);
        assertThat(actual.getNumber()).isEqualTo(expected.getNumber());
        assertThat(actual.getFibonacci_value()).isEqualTo(expected.getFibonacci_value());
        assertEquals(1, list.size());
    }

    @Test
    @Order(3)
    @DisplayName("Fibonacci service test")
    public void fibonacciServiceTest() {
        // !!!!!  initial repository contain one object -> Fibonacci(number-10, value-55)
        // SERVICE TEST
        // find and return fibonacci value of 7 from repository if exist,
        // if obj. not exist - calculate fib.value of 7, create fibonacci object, add in DB and return fib. value
        long seven = fibonacciService.getFibonacciValueByNumber(7L);
        assertThat(seven).isEqualTo(13L);
        assertThat(fibonacciRepository.count()).isEqualTo(2);

        // try ccreate duplicate with number 7 and value 13
        long value = fibonacciService.getFibonacciValueByNumber(7L);
        assertThat(13L).isEqualTo(value);
        //if duplicate was not created - object already exist, objects count should be 2, if created - 3
        assertThat(fibonacciRepository.count()).isEqualTo(2);

        List<Fibonacci> duplicate = jdbcTemplate
                .query("SELECT * FROM fibonacci WHERE number = 7 AND fibonacci_value = 13", (resultSet, rowsNumber) -> {
                            Fibonacci fibonacci = new Fibonacci();
                            fibonacci.setId(resultSet.getLong("id"));
                            fibonacci.setNumber(resultSet.getLong("number"));
                            fibonacci.setFibonacci_value(resultSet.getLong("fibonacci_value"));
                            return fibonacci;
                        }
                );
        assertThat(duplicate.size()).isEqualTo(1);
        assertThat(duplicate.get(0).getFibonacci_value()).isEqualTo(13L);
        assertThat(duplicate.get(0).getNumber()).isEqualTo(7L);
    }

    @Test
    @Order(4)
    @DisplayName("Fibonacci exception test")
    public void fibonacciExceptionTest() {
        //EXCEPTION TEST - try get fibonacci value from number < zero
       Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fibonacciService.getFibonacciValueByNumber(-1L);
        });
        //exception message from .../service/calculator
        assertThat(exception.getMessage()).isEqualTo("number should be >= zero");
        assertThat(exception.getClass()).isEqualTo(IllegalArgumentException.class);
    }
}
