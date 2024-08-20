package org.example.springexample.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "fibonacci")
public class Fibonacci {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number")
    private Long number;
    @Column(name = "fibonacci_value")
    private Long fibonacci_value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getFibonacci_value() {
        return fibonacci_value;
    }

    public void setFibonacci_value(Long fibonacci_value) {
        this.fibonacci_value = fibonacci_value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fibonacci fibonacci = (Fibonacci) o;
        return Objects.equals(id, fibonacci.id) && Objects.equals(number, fibonacci.number) && Objects.equals(fibonacci_value, fibonacci.fibonacci_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, fibonacci_value);
    }
}
