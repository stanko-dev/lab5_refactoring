package com.restaurant.repository;

import com.restaurant.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findById(int id);
    List<Customer> findAll();
    boolean existsById(int id);
}
