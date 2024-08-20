package org.example.springexample.Repository;

import org.example.springexample.Entity.Fibonacci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FibonacciRepository extends JpaRepository<Fibonacci, Long> {
    @Query("SELECT f FROM Fibonacci f WHERE f.number = :number")
    List<Fibonacci> findByNumber(@Param("number") Long number);

    @Query("SELECT COUNT(f) > 0 FROM Fibonacci f WHERE f.number = :number")
    boolean exist(@Param("number") Long number);

    default void saveIfNotExist(Fibonacci fibonacci) {
        if (!exist(fibonacci.getNumber())) {
            save(fibonacci);
        }
    }
}
