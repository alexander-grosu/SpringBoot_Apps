package org.example.springexample;

import jakarta.persistence.EntityManager;
import org.example.springexample.Entity.Fibonacci;
import org.example.springexample.Repository.FibonacciRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("DataJpa Test")
public class DataJpaRepositoryTest {
    @Autowired
    private FibonacciRepository fibonacciRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void fillingDB() {
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.setNumber(10L);
        fibonacci.setFibonacci_value(55L);
        // add obj. if not exist
        fibonacciRepository.saveIfNotExist(fibonacci);
        entityManager.flush();
        entityManager.detach(fibonacci);
    }

    @AfterEach
    public void clearDB() {
        fibonacciRepository.deleteAll();
    }

    @Test
    @DisplayName("add new object in database test")
    void repositoryAddNewObjTest() {
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.setNumber(9L);
        fibonacci.setFibonacci_value(34L);
        // save object if not exist
        fibonacciRepository.saveIfNotExist(fibonacci);
        entityManager.flush();
        entityManager.detach(fibonacci);
        // initially is one obj. fibonacci(10,55) in db
        // CHECK if obj. fibonacci(9,34) is add in db
        List<Fibonacci> fibonacciList = jdbcTemplate
                .query("SELECT * FROM fibonacci WHERE number = 9", (resultSet, rowsNumber) -> {
                            Fibonacci fib = new Fibonacci();
                            fib.setId(resultSet.getLong("id"));
                            fib.setNumber(resultSet.getLong("number"));
                            fib.setFibonacci_value(resultSet.getLong("fibonacci_value"));
                            return fib;
                        }
                );
        assertThat(fibonacciList.size()).isEqualTo(1);
        assertThat(fibonacciList.get(0).getFibonacci_value()).isEqualTo(34L);
    }

    @Test
    @DisplayName("repository method findByNumber test")
    void repositoryFindByNumberTest() {
        // initially is one obj. fibonacci(number=10, fibonacci_value=55) created in ForEach method
        List<Fibonacci> list = jdbcTemplate
                .query("SELECT * FROM fibonacci WHERE number = 10", (resultSet, rowsNumber) -> {
                            Fibonacci fib = new Fibonacci();
                            fib.setId(resultSet.getLong("id"));
                            fib.setNumber(resultSet.getLong("number"));
                            fib.setFibonacci_value(resultSet.getLong("fibonacci_value"));
                            return fib;
                        }
                );
        Fibonacci fibOne = fibonacciRepository.findByNumber(10L).get(0);
        Fibonacci fibTwo = list.get(0);
        assertThat(fibOne).isEqualTo(fibTwo);
    }

    @Test
    @DisplayName("repository method saveIfNotExist test")
    void saveIfNotExistTest(){
        // initially is only one obj. fibonacci(number=10, fibonacci_value=55) created in ForEach method
        // create duplicate of obj. fibonacci(10,55) and save
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.setNumber(10L);
        fibonacci.setFibonacci_value(55L);
        fibonacciRepository.saveIfNotExist(fibonacci);
        entityManager.flush();
        entityManager.detach(fibonacci);
        // check if duplicate not save
        List<Fibonacci> list = jdbcTemplate
                .query("SELECT * FROM fibonacci WHERE number = 10", (resultSet, rowsNumber) -> {
                            Fibonacci fib = new Fibonacci();
                            fib.setId(resultSet.getLong("id"));
                            fib.setNumber(resultSet.getLong("number"));
                            fib.setFibonacci_value(resultSet.getLong("fibonacci_value"));
                            return fib;
                        }
                );
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getNumber()).isEqualTo(fibonacci.getNumber());
    }
}
