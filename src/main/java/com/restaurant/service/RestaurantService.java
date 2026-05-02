package com.restaurant.service;

import com.restaurant.dto.CustomerDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Customer;
import com.restaurant.model.Dish;
import com.restaurant.model.Order;
import com.restaurant.model.OrderStatus;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.DishRepository;
import com.restaurant.repository.OrderRepository;

import java.util.List;

public class RestaurantService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DishRepository dishRepository;
    private int nextOrderId = 1;

    public RestaurantService(OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.dishRepository = dishRepository;
    }

    // Сценарій 1: Розміщення замовлення
    public OrderDTO placeOrder(int customerId, List<String> dishNames) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        if (dishNames == null || dishNames.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one dish");
        }

        List<Dish> dishes = dishNames.stream()
                .map(name -> dishRepository.findByName(name).stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Dish not found: " + name)))
                .toList();

        Order order = new Order(nextOrderId++, customer, dishes);
        orderRepository.save(order);
        return toDTO(order);
    }

    // Сценарій 2: Скасування замовлення
    public OrderDTO cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed order");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return toDTO(order);
    }

    // Сценарій 3: Пошук страв за назвою
    public List<Dish> findDishesByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Search name cannot be blank");
        }
        return dishRepository.findByName(name);
    }

    // Сценарій 4: Реєстрація клієнта
    public CustomerDTO registerCustomer(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        if (customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer with id " + id + " already exists");
        }
        Customer customer = new Customer(id, name);
        customerRepository.save(customer);
        return new CustomerDTO(customer.getId(), customer.getName());
    }

    private OrderDTO toDTO(Order order) {
        List<String> names = order.getDishes().stream().map(Dish::getName).toList();
        return new OrderDTO(order.getId(), order.getCustomer().getName(), names, order.getTotalPrice(), order.getStatus().name());
    }
}
