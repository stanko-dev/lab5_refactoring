package com.restaurant.repository;

import com.restaurant.model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public void save(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");
        if (existsById(customer.getId())) throw new IllegalArgumentException("Customer with id " + customer.getId() + " already exists");
        customers.add(customer);
    }

    @Override
    public Optional<Customer> findById(int id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(customers);
    }

    @Override
    public boolean existsById(int id) {
        return customers.stream().anyMatch(c -> c.getId() == id);
    }
}
