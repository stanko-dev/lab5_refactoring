package com.restaurant.repository;

import com.restaurant.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(int id);
    List<Order> findAll();
    void deleteById(int id);
}
