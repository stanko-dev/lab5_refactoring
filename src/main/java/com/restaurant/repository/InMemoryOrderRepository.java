package com.restaurant.repository;

import com.restaurant.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public int nextId() {
        return counter.getAndIncrement();
    }

    @Override
    public void save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orders.add(order);
    }

    @Override
    public Optional<Order> findById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    @Override
    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    @Override
    public void deleteById(int id) {
        orders.removeIf(o -> o.getId() == id);
    }
}
